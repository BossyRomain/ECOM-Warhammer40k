package com.warhammer.ecom.config;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AllegianceService allegianceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductImageService productImageService;

    @PostConstruct
    public void initDB() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dev/dev-db-init.json");

            JsonNode rootNode = objectMapper.readTree(inputStream);

            // Chargement des allégeances
            List<Allegiance> jsonAllegiances = objectMapper.convertValue(rootNode.get("allegiances"), new TypeReference<List<Allegiance>>() {
            });
            List<Allegiance> allegiances = new ArrayList<>();
            for (Allegiance allegiance : jsonAllegiances) {
                allegiances.add(allegianceService.create(allegiance));
            }

            // Chargement des produits
            JsonNode jsonProductsArray = rootNode.get("products");
            List<Product> products = new ArrayList<>();
            for (JsonNode productJson : jsonProductsArray) {
                int allegianceIndex = productJson.has("allegianceIndex") ? productJson.get("allegianceIndex").asInt() : -1;

                Product product = objectMapper.convertValue(productJson, Product.class);
                if (allegianceIndex > -1) {
                    product.setAllegiance(allegiances.get(allegianceIndex));
                }
                product.setImages(new ArrayList<>());
                products.add(productService.create(product));
            }

            // Chargement des images des produits
            JsonNode productsImagesArray = rootNode.get("productsImages");
            Path tempFile = Files.createTempFile("productImage", ".jpg");
            for (JsonNode productImageNode : productsImagesArray) {
                int productIndex = productImageNode.get("productIndex").asInt();
                Product product = products.get(productIndex);
                String name = productImageNode.get("name").asText();
                String description = productImageNode.get("description").asText();
                boolean isCatalogueImage = productImageNode.has("catalogueImg") && productImageNode.get("catalogueImg").asBoolean();

                InputStream imgInputStream = getClass().getClassLoader().getResourceAsStream("dev/productImages/" + name);
                Files.copy(imgInputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();

                MultipartFile multipartFile = new ImgMultipartFile(tempFile.toFile(), name);
                productImageService.create(multipartFile, product.getId(), description, isCatalogueImage);
            }
            Files.deleteIfExists(tempFile);

            // Chargement des utilisateurs
            List<User> users = objectMapper.convertValue(rootNode.get("users"), new TypeReference<List<User>>() {
            });
            for (User user : users) {
                userService.create(user);
            }

            // Chargement des clients
            List<ClientSignUpDTO> ClientSignUpDTOs = objectMapper.convertValue(rootNode.get("clients"), new TypeReference<List<ClientSignUpDTO>>() {
            });
            for (ClientSignUpDTO clientSignUpDTO : ClientSignUpDTOs) {
                clientSignUpDTO.setBirthday(LocalDate.now());
                clientService.create(clientSignUpDTO);
            }
            LoggerFactory.getLogger(DatabaseInitializer.class).info("Database initialized");
        } catch (AmazonS3Exception ignored) {
            System.out.println("No access to the S3 bucket");
        } catch (Exception e) {
            System.out.println("Error while initializing database");
            e.printStackTrace();
        }
    }

    /***
     * Supprime les fichiers des images des produits du dossier assets de l'application angular.
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
                    Files.delete(file);  // Delete each file
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);  // After visiting all files, delete the directory
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
        }
    }

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
