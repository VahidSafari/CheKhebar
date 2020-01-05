

data class PlaceDetailResponse (

    val meta : DetailMeta,
    val response : DetailResponse
)

data class DetailMeta (

    val code : Int,
    val requestId : String
)

data class DetailResponse (

    val venue : DetailVenue
)

data class DetailVenue (

    val id : String,
    val name : String,
    val contact : DetailContact,
    val location : DetailLocation,
    val canonicalUrl : String,
    val categories : List<DetailCategories>,
    val verified : Boolean,
    val stats : DetailStats,
    val likes : Likes,
    val dislike : Boolean,
    val ok : Boolean,
    val rating : Double,
    val ratingColor : String,
    val ratingSignals : Int,
    val allowMenuUrlEdit : Boolean,
    val beenHere : DetailBeenHere,
    val specials : DetailSpecials,
    val photos : DetailPhotos,
    val reasons : Reasons,
    val hereNow : HereNow,
    val createdAt : Int,
    val tips : Tips,
    val shortUrl : String,
    val timeZone : String,
    val listed : Listed,
    val pageUpdates : PageUpdates,
    val inbox : Inbox,
    val venueChains : List<String>,
    val attributes : Attributes,
    val bestPhoto : BestPhoto,
    val colors : Colors
)

data class DetailContact (

    val phone : Int,
    val formattedPhone : String
)

data class DetailLocation (

    val address : String,
    val lat : Double,
    val lng : Double,
    val labeledLatLngs : List<LabeledLatLngs>,
    val cc : String,
    val neighborhood : String,
    val city : String,
    val state : String,
    val country : String,
    val formattedAddress : List<String>
)

data class DetailCategories (

    val id : String,
    val name : String,
    val pluralName : String,
    val shortName : String,
    val icon : Icon,
    val primary : Boolean
)

data class DetailStats (

    val tipCount : Int,
    val usersCount : Int,
    val checkinsCount : Int,
    val visitsCount : Int
)

data class Likes (

    val count : Int,
    val groups : List<Groups>,
    val summary : String
)

data class DetailBeenHere (

    val count : Int,
    val unconfirmedCount : Int,
    val marked : Boolean,
    val lastCheckinExpiredAt : Int
)

data class DetailSpecials (

    val count : Int,
    val items : List<String>
)

data class DetailPhotos (

    val count : Int,
    val groups : List<Groups>
)

data class Reasons (

    val count : Int,
    val items : List<Items>
)

data class HereNow (

    val count : Int,
    val summary : String,
    val groups : List<String>
)

data class Tips (

    val count : Int,
    val groups : List<Groups>
)

data class Listed (

    val count : Int,
    val groups : List<Groups>
)

data class PageUpdates (

    val count : Int,
    val items : List<String>
)

data class Inbox (

    val count : Int,
    val items : List<String>
)

data class Attributes (

    val groups : List<Groups>
)

data class BestPhoto (

    val id : String,
    val createdAt : Int,
    val source : Source,
    val prefix : String,
    val suffix : String,
    val width : Int,
    val height : Int,
    val visibility : String
)

data class Colors (

    val highlightColor : HighlightColor,
    val highlightTextColor : HighlightTextColor,
    val algoVersion : Int
)

data class LabeledLatLngs (

    val label : String,
    val lat : Double,
    val lng : Double
)

data class Icon (

    val prefix : String,
    val suffix : String
)

data class Groups (

    val type : String,
    val name : String,
    val summary : String,
    val count : Int,
    val items : List<Items>
)

data class HighlightColor (

    val photoId : String,
    val value : Int
)

data class Followers (

    val count : Int
)


data class HighlightTextColor (

    val photoId : String,
    val value : Int
)

data class Items (

    val displayName : String,
    val displayValue : String
)

data class ListItems (

    val count : Int,
    val items : List<Items>
)

data class Photo (

    val id : String,
    val createdAt : Int,
    val prefix : String,
    val suffix : String,
    val width : Int,
    val height : Int,
    val user : User,
    val visibility : String
)

data class Source (

    val name : String,
    val url : String
)

data class Todo (

    val count : Int
)

data class User (

    val id : Int,
    val firstName : String,
    val gender : String,
    val photo : Photo
)