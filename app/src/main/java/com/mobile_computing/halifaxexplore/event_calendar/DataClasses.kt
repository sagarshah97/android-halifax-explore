data class EventResponse(
    val results: List<Event>
)

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val labels: List<String>,
    val entities: List<Venue>
    // Add other fields as needed from the API response
)
data class Venue(
    val entity_id: String,
    val name: String,
    val type: String,
    val formatted_address: String
)


// You might need additional data classes for nested objects if necessary
