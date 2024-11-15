package com.warhammer.ecom.config;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/***
 * Classe utilisée pour initialiser la base de données avec des données uniquement dans l'environnement de développement.
 */
@Component
public class DevDatabaseInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private AllegianceService allegianceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    private final String DEV_RES_PATH = System.getProperty("dev.resources");

    private final String ANGULAR_ASSETS_PATH = System.getProperty("angular.assets");

    @PostConstruct
    public void initDB() {
        try(InputStream inputStream = new FileInputStream(DEV_RES_PATH + "/dev-db-init.json")) {
            if(inputStream == null) {
                throw new RuntimeException("dev-db-init.json not found");
            }

            try {
                Files.createDirectory(Paths.get(ANGULAR_ASSETS_PATH + "/dev/images"));
            } catch (IOException ignored) {
            }

            JsonNode rootNode = objectMapper.readTree(inputStream);

            // Chargement des allégeances
            List<Allegiance> jsonAllegiances = objectMapper.convertValue(rootNode.get("allegiances"), new TypeReference<List<Allegiance>>() {});
            List<Allegiance> allegiances = new ArrayList<>();
            for(Allegiance allegiance : jsonAllegiances) {
                allegiances.add(allegianceService.create(allegiance));
            }

            // Chargement des produits
            JsonNode jsonProductsArray = rootNode.get("products");
            List<Product> products = new ArrayList<>();
            for(JsonNode productJson: jsonProductsArray) {
                int allegianceIndex = productJson.has("allegianceIndex") ? productJson.get("allegianceIndex").asInt() : -1;

                Product product = objectMapper.convertValue(productJson, Product.class);
                if(allegianceIndex > -1) {
                    product.setAllegiance(allegiances.get(allegianceIndex));
                }
                products.add(productService.create(product));
            }

            // Chargement des images des produits
            JsonNode productsImagesArray = rootNode.get("productsImages");
            for(JsonNode productImageNode : productsImagesArray) {
                int productIndex = productImageNode.get("productIndex").asInt();
                String url = productImageNode.get("url").asText();
                String description = productImageNode.get("description").asText();
                boolean isCatalogueImage = productImageNode.has("catalogueImg") && productImageNode.get("catalogueImg").asBoolean();

                productImageService.create(url, description, isCatalogueImage, products.get(productIndex));
            }

            // Chargement des utilisateurs
            List<User> users = objectMapper.convertValue(rootNode.get("users"), new TypeReference<List<User>>() {});
            for(User user : users) {
                userService.create(user);
            }

            // Chargement des clients
            List<ClientSignUpDTO> ClientSignUpDTOs = objectMapper.convertValue(rootNode.get("clients"), new TypeReference<List<ClientSignUpDTO>>() {});
            for(ClientSignUpDTO clientSignUpDTO : ClientSignUpDTOs) {
                clientSignUpDTO.setBirthday(LocalDate.now());
                clientService.create(clientSignUpDTO);
            }

            // Copie des fichiers des images des produits dans le dossier assets de l'application Angular
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(DEV_RES_PATH + "/productImages"));
            Path targetDir = Paths.get(ANGULAR_ASSETS_PATH + "/dev/images/");
            for(Path entry: stream) {
                Path targetFile = targetDir.resolve(entry.getFileName());
                Files.copy(entry, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Supprime les fichiers des images des produits du dossier assets de l'application angular.
     */
    @PreDestroy
    public void onShutDown() {
        try {
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
            e.printStackTrace();
            System.out.println("Error during delete");
        }
    }
}
