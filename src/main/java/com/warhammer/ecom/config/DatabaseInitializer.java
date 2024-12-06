package com.warhammer.ecom.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.AllegianceRepository;
import com.warhammer.ecom.repository.ProductRepository;
import com.warhammer.ecom.repository.UserRepository;
import com.warhammer.ecom.service.ClientService;
import com.warhammer.ecom.service.ProductImageService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Component to initialize the database each time the backend starts.
 */
@Component
public class DatabaseInitializer {

    private static final String DB_INIT_FILES_PATH = "db/init";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private AllegianceRepository allegianceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initDB() {
        try {
            initUsers();
            initAllegiances();
            initProducts();
        } catch (DataIntegrityViolationException e) {
            LoggerFactory.getLogger(DatabaseInitializer.class).warn("The database seem to be already initialized.");
        } catch (Exception e) {
            LoggerFactory.getLogger(DatabaseInitializer.class).error("Error while initializing database");
            e.printStackTrace();
        }
    }

    /***
     * Remove the files of the products' images from the assets folder of the angular application.
     */
    @PreDestroy
    @Profile("dev")
    public void onShutDown() {
        try {
            final String ANGULAR_ASSETS_PATH = System.getProperty("angular.assets");
            Path directoryPath = Paths.get(ANGULAR_ASSETS_PATH + "/dev/images/");
            Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LoggerFactory.getLogger(DatabaseInitializer.class).error("Error while shuting down the database initializer");
            e.printStackTrace();
        }
    }

    /**
     * Add the administrators and the clients in the database.
     */
    private void initUsers() throws Exception {
        LoggerFactory.getLogger(DatabaseInitializer.class).info("Start adding the users (clients and admins)");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_INIT_FILES_PATH + "/db-init-users.json");
        JsonNode rootNode = objectMapper.readTree(inputStream);
        inputStream.close();

        // Chargement des administrateurs
        userRepository.saveAll(
            objectMapper.convertValue(rootNode.get("admins"), new TypeReference<List<User>>() {
            })
        );

        // Chargement des clients
        List<ClientSignUpDTO> clients = objectMapper.convertValue(rootNode.get("clients"), new TypeReference<List<ClientSignUpDTO>>() {
        });

        final long d1 = LocalDate.of(1979, 1, 1).toEpochDay();
        final long d2 = LocalDate.of(2004, 1, 1).toEpochDay();
        final Random random = new Random();
        for (ClientSignUpDTO client : clients) {
            client.setBirthday(LocalDate.ofEpochDay(d1 + random.nextLong(d2 - d1)));
            clientService.create(client, false);
        }

        LoggerFactory.getLogger(DatabaseInitializer.class).info("Users successfully added");
    }

    /**
     * Add the allegiances of the products in the database.
     */
    private void initAllegiances() throws Exception {
        LoggerFactory.getLogger(DatabaseInitializer.class).info("Start adding the allegiances");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_INIT_FILES_PATH + "/db-init-allegiances.json");
        JsonNode rootNode = objectMapper.readTree(inputStream);
        inputStream.close();

        allegianceRepository.saveAll(
            objectMapper.convertValue(rootNode, new TypeReference<List<Allegiance>>() {
            })
        );

        LoggerFactory.getLogger(DatabaseInitializer.class).info("Allegiances successfully added");
    }

    /**
     * Add the products and their images in the database.
     */
    private void initProducts() throws Exception {
        LoggerFactory.getLogger(DatabaseInitializer.class).info("Start adding the products");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_INIT_FILES_PATH + "/db-init-products.json");
        JsonNode rootNode = objectMapper.readTree(inputStream);
        inputStream.close();

        final List<Allegiance> allegiances = allegianceRepository.findAll();

        // Chargement des produits
        Hashtable<String, Product> productsImgsPrefixs = new Hashtable<>();
        for (JsonNode productJson : rootNode) {
            String faction = productJson.has("faction") ? productJson.get("faction").asText() : null;

            Product product = objectMapper.convertValue(productJson, Product.class);
            if (faction != null) {
                Allegiance allegiance = allegiances.stream().filter(a -> a.getFaction().name().equals(faction)).findFirst().orElse(null);
                product.setAllegiance(allegiance);
            } else {
                product.setAllegiance(null);
            }
            product.setImages(new ArrayList<>());
            productRepository.save(product);
            productsImgsPrefixs.put(productJson.get("imgsPrefix").asText(), product);
        }

        // Chargement des images des produits
        final String dir = DB_INIT_FILES_PATH + "/products_images";
        Stream<Path> paths = Files.walk(Path.of(getClass().getClassLoader().getResource(dir).toURI()));
        paths.filter(Files::isRegularFile)
            .forEach(path -> {
                try {
                    String filename = path.getFileName().toString();
                    String name = filename.substring(0, filename.indexOf('.'));
                    String prefix = name.substring(0, name.lastIndexOf('_'));
                    String extension = filename.substring(filename.indexOf('.'));

                    Product p = productsImgsPrefixs.get(prefix);
                    boolean isCatalogueImage = name.contains("_0");

                    InputStream in = getClass().getClassLoader().getResourceAsStream(dir + "/" + filename);
                    File tempFile = inputStreamToFile(in, extension);
                    in.close();

                    MultipartFile multipartFile = new ImgMultipartFile(tempFile, filename);
                    productImageService.create(multipartFile, p.getId(), "", isCatalogueImage);

                    tempFile.delete();
                } catch (IOException ignore) {
                }
            });

        LoggerFactory.getLogger(DatabaseInitializer.class).info("Products successfully added");
    }

    /**
     * Converts an input stream into a file.
     *
     * @param inputStream an InputStream
     * @param extension   a file extension
     * @return a file with the same content in the input stream
     * @throws IOException a IOException
     */
    private File inputStreamToFile(InputStream inputStream, String extension) throws IOException {
        File tempFile = Files.createTempFile("temp", extension).toFile();
        FileOutputStream outputStream = new FileOutputStream(tempFile, false);
        byte[] buffer = new byte[8192];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    /**
     * Converts a file of a product image into a MultipartFile.
     */
    private static class ImgMultipartFile implements MultipartFile {

        private File file;

        private String name;

        public ImgMultipartFile(File file, String name) {
            this.file = file;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return "multipart/form-data";
        }

        @Override
        public boolean isEmpty() {
            return file.length() == 0;
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public byte[] getBytes() throws IOException {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            return bytes;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            InputStream inputStream = getInputStream();
            FileOutputStream outputStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
