package com.example.chekhebar.data

data class PlaceDetailResponse (

    val meta : DetailMeta,
    val response : DetailResponse
)

data class DetailMeta (

    val code : Int,
    val errorType: String? = null,
    val errorDetail: String? = null,
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
    val reasons : DetailReasons,
    val hereNow : DetailHereNow,
    val createdAt : Int,
    val tips : DetailTips,
    val shortUrl : String,
    val timeZone : String,
    val listed : DetailListed,
    val pageUpdates : DetailPageUpdates,
    val inbox : DetailInbox,
    val venueChains : List<String>,
    val attributes : DetailAttributes,
    val bestPhoto : DetailBestPhoto,
    val colors : DetailColors
)

data class DetailContact (

    val phone : String?,
    val formattedPhone : String?
)

data class DetailLocation (

    val address : String?,
    val lat : Double,
    val lng : Double,
    val labeledLatLngs : List<DetailLabeledLatLngs>,
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
    val icon : DetailIcon,
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
    val groups : List<DetailGroups>,
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
    val groups : List<DetailGroups>
)

data class DetailReasons (

    val count : Int,
    val items : List<DetailItems>
)

data class DetailHereNow (

    val count : Int,
    val summary : String,
    val groups : List<String>
)

data class DetailTips (

    val count : Int,
    val groups : List<DetailGroups>
)

data class DetailListed (

    val count : Int,
    val groups : List<DetailGroups>
)

data class DetailPageUpdates (

    val count : Int,
    val items : List<String>
)

data class DetailInbox (

    val count : Int,
    val items : List<String>
)

data class DetailAttributes (

    val groups : List<DetailGroups>
)

data class DetailBestPhoto (

    val id : String,
    val createdAt : Int,
    val source : Source,
    val prefix : String,
    val suffix : String,
    val width : Int,
    val height : Int,
    val visibility : String
)

data class DetailColors (

    val highlightColor : DetailHighlightColor,
    val highlightTextColor : DetailHighlightTextColor,
    val algoVersion : Int
)

data class DetailLabeledLatLngs (

    val label : String,
    val lat : Double,
    val lng : Double
)

data class DetailIcon (

    val prefix : String,
    val suffix : String
)

data class DetailGroups (

    val type : String,
    val name : String,
    val summary : String,
    val count : Int,
    val items : List<DetailItems>
)

data class DetailHighlightColor (

    val photoId : String,
    val value : Int
)

data class DetailFollowers (

    val count : Int
)


data class DetailHighlightTextColor (

    val photoId : String,
    val value : Int
)

data class DetailItems (

    val displayName : String,
    val displayValue : String
)

data class ListItems (

    val count : Int,
    val items : List<DetailItems>
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