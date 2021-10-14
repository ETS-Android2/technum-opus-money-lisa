package com.technumopus.moneylisa.model;

public class CategoryBean {
    int categoryId;
    String catName; // just use category name for now, in future we may implement id with vector icons
    String catVector;


    public CategoryBean() {
    }

    public CategoryBean(String catName) {
        this.catName = catName;
    }

    public CategoryBean(String catName, String catVector) {
        this.catName = catName;
        this.catVector = catVector;
    }

    public CategoryBean(int categoryId, String catName, String catVector) {
        this.categoryId = categoryId;
        this.catName = catName;
        this.catVector = catVector;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatVector() {
        return catVector;
    }

    public void setCatVector(String catVector) {
        this.catVector = catVector;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "categoryId=" + categoryId +
                ", catName='" + catName + '\'' +
                ", catVector='" + catVector + '\'' +
                '}';
    }
}
