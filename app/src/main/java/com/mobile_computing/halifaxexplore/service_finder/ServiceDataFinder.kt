// ServiceDataProvider.kt
package com.mobile_computing.halifaxexplore.service_finder

object ServiceDataProvider {
    val allServiceProviders: List<ServiceProvider> by lazy {
        createServiceProviders()
    }

    private fun createServiceProviders(): List<ServiceProvider> {
        return listOf(
            ServiceProvider("Service Nova Scotia", "Not Available", "(902) 424-5200", "Halifax, NS", "https://novascotia.ca/dma/"),
            ServiceProvider("Department of Community Service", "Not Available", "+1 877-424-1177", "Halifax, NS", "https://novascotia.ca/coms/"),
            ServiceProvider("Scotia Tire Service Limited", "Not Available", "(902) 454-8300", "Halifax, NS", "https://www.scotiatire.ca/"),
            ServiceProvider("GDI Integrated Facility services", "Not Available", "(902) 420-1497", "Dartmouth, NS", "gdi.com"),
            ServiceProvider("H& A services", "Not Available", "(902) 430-5055", "Not Available", "Not Available"),
            ServiceProvider("Carlo Auto Service Limited", "Not Available", "(902) 455-5576", "Halifax, NS", "Not Available"),
            ServiceProvider("Golden service mobile Bar and Catering", "(902) 497-0006", "Not Available", "Halifax, NS", "goldenservice.ca"),
            ServiceProvider("The UPS Store", "Not Available", "(902) 453-4444", "Halifax, NS", "https://www.theupsstore.ca/233/"),
            ServiceProvider("Aris & B Auto Service", "Not Available", "(902) 425-0088", "Halifax, NS", "Not Available"),
            ServiceProvider("Shell", "Not Available", "(902) 493-5176", "Halifax, NS", "https://find.shell.com/ca/fuel/10053607-robie-st-halifax"),
            ServiceProvider("Blue Wave face Cleaning Services", "(902) 906-9804", "Not Available", "Halifax, NS", "https://bluewavecleaning.ca/office-cleaning-services-halifax-ns/"),
            ServiceProvider("Coast Tire and Auto Service", "Not Available", "(902) 455-0504", "Halifax, NS", "https://www.coasttire.com/"),
            ServiceProvider("Delta Cleaning Services Limited", "(902) 431-3232", "Not Available", "Halifax, NS", "deltacleaningservices.ca"),
            ServiceProvider("Staples Print & Marketing Services", "(902) 474-5101", "Not Available", "Halifax, NS", "https://stores.staplescopyandprint.ca/ns/halifax/services-d-impressions-et-de-marketing-ca-cp-148.html"),
            ServiceProvider("Speedy Auto Service Halifax Robie", "(902) 455-5487", "Not Available", "Halifax, NS", "https://www.speedy.com/en-ca/shop/halifax-robie/")
        )
    }
}
