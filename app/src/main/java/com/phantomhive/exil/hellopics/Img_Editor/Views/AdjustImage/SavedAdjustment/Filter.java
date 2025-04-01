package com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.SavedAdjustment;




public class Filter {
    private String id;
    private String name;
    private float[] filterValues;

    // Default constructor
    public Filter() {}

    public Filter(String id, String name, float[] filterValues) {
        this.id = id;
        this.name = name;
        this.filterValues = filterValues;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(float[] filterValues) {
        this.filterValues = filterValues;
    }

    // Helper method to convert float array to string
    public static String filterValuesToString(float[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i < values.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    // Helper method to convert string to float array
    public static float[] stringToFilterValues(String valuesStr) {
        if (valuesStr == null || valuesStr.isEmpty()) {
            return new float[0];
        }

        String[] valueStrArray = valuesStr.split(",");
        float[] values = new float[valueStrArray.length];

        for (int i = 0; i < valueStrArray.length; i++) {
            values[i] = Float.parseFloat(valueStrArray[i]);
        }

        return values;
    }
}