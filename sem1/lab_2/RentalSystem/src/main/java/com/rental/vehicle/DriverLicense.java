package com.rental.vehicle;

public class DriverLicense {
        private String licenseNumber;
        private String categories;
        private String issuingAuthority;

        public DriverLicense() {
        }

        public DriverLicense(String licenseNumber, String categories, String issuingAuthority) {
            this.licenseNumber = licenseNumber;
            this.categories = categories;
            this.issuingAuthority = issuingAuthority;
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        public String getCategories() {
            return categories;
        }

        public String getIssuingAuthority() {
            return issuingAuthority;
        }

        public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public void setIssuingAuthority(String issuingAuthority) {
            this.issuingAuthority = issuingAuthority;
        }

        public boolean hasCategoryB() {
            return categories != null && categories.toUpperCase().contains("B");
        }

        public boolean isRequired() {
            return categories != null && !categories.isEmpty();
        }
}
