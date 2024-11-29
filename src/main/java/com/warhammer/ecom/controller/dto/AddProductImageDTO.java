package com.warhammer.ecom.controller.dto;

public class AddProductImageDTO {

    String description;

    boolean isCatalogueImage;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCatalogueImage() {
        return isCatalogueImage;
    }

    public void setCatalogueImage(boolean catalogueImage) {
        isCatalogueImage = catalogueImage;
    }
}
