package com.warhammer.ecom.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.service.AllegianceService;
import com.warhammer.ecom.service.ProductImageService;
import com.warhammer.ecom.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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

    @PostConstruct
    public void initDB() {
        final String devResPath = System.getProperty("DEV_FOLDER_PATH");
        final String angularAssetsPath = System.getProperty("ANGULAR_ASSETS_PATH");

        try(InputStream inputStream = new FileInputStream(devResPath + "/dev-db-init.json")) {
            if(inputStream == null) {
                throw new RuntimeException("dev-db-init.json not found");
            }

            try {
                Files.createDirectory(Paths.get(angularAssetsPath + "/dev/images"));
            } catch (IOException e) {
            }

            JsonNode rootNode = objectMapper.readTree(inputStream);

            List<Allegiance> jsonAllegiances = objectMapper.convertValue(rootNode.get("allegiances"), new TypeReference<List<Allegiance>>() {});
            List<Allegiance> allegiances = new ArrayList<>();
            for(Allegiance allegiance : jsonAllegiances) {
                allegiances.add(allegianceService.create(allegiance));
            }

            JsonNode jsonProductsArray = rootNode.get("products");
            List<Product> products = new ArrayList<>();
            for(JsonNode productJson: jsonProductsArray) {
                int allegianceIndex = productJson.has("allegianceIndex") ? productJson.get("allegianceIndex").asInt() : -1;

                Product product = objectMapper.convertValue(productJson, Product.class);
                if(allegianceIndex > -1) {
                    product.setAllegiance(allegiances.get(allegianceIndex));
                }
                products.add(productService.createProduct(product));
            }

            JsonNode productsImagesArray = rootNode.get("productsImages");
            for(JsonNode productImageNode : productsImagesArray) {
                int productIndex = productImageNode.get("productIndex").asInt();
                String url = productImageNode.get("url").asText();
                String description = productImageNode.get("description").asText();
                boolean isCatalogueImage = productImageNode.has("catalogueImg") && productImageNode.get("catalogueImg").asBoolean();

                productImageService.create(url, description, isCatalogueImage, products.get(productIndex));
            }

            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(devResPath + "/productImages"));
            Path targetDir = Paths.get(angularAssetsPath + "/dev/images/");
            for(Path entry: stream) {
                Path targetFile = targetDir.resolve(entry.getFileName());
                Files.copy(entry, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void onShutDown() {
        try {
            final String angularAssetsPath = System.getProperty("ANGULAR_ASSETS_PATH");
            Path directoryPath = Paths.get(angularAssetsPath + "/dev/images/");
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
