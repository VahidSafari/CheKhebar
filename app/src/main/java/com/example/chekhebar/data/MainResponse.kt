package com.example.chekhebar.data


data class MainResponse (

	val meta : Meta,
	val response : Response
)

data class Meta (

	val code : Int,
	val errorType: String? = null,
	val errorDetail: String? = null,
	val requestId : String
)

data class Response (

	val suggestedFilters : SuggestedFilters,
	val suggestedRadius : Int,
	val headerLocation : String,
	val headerFullLocation : String,
	val headerLocationGranularity : String,
	val totalResults : Int,
	val suggestedBounds : SuggestedBounds,
	val groups : List<Groups>
)

data class SuggestedFilters (

	val header : String,
	val filters : List<Filters>
)

data class SuggestedBounds (

	val ne : Ne,
	val sw : Sw
)

data class Ne (

	val lat : Double,
	val lng : Double
)

data class Sw (

	val lat : Double,
	val lng : Double
)

data class Filters (

	val name : String,
	val key : String
)

data class Groups (

	val type : String,
	val name : String,
	val items : List<Items>
)

data class Items (

	val reasons : Reasons,
	val venue : Venue,
	val referralId : String
)

data class Reasons (

	val count : Int,
	val items : List<Items>
)

data class Venue (

	val id : String,
	val name : String,
	val contact : Contact,
	val location : Location,
	val categories : List<Categories>,
	val verified : Boolean,
	val stats : Stats,
	val beenHere : BeenHere,
	val photos : Photos,
	val hereNow : HereNow
)

class Contact

data class Location (

	val address : String,
	val lat : Double,
	val lng : Double,
	val labeledLatLngs : List<LabeledLatLngs>,
	val distance : Int,
	val cc : String,
	val neighborhood : String,
	val city : String,
	val state : String,
	val country : String,
	val formattedAddress : List<String>
)

data class Categories (

	val id : String,
	val name : String,
	val pluralName : String,
	val shortName : String,
	val icon : Icon,
	val primary : Boolean
)

data class Stats (

	val tipCount : Int,
	val usersCount : Int,
	val checkinsCount : Int,
	val visitsCount : Int
)

data class BeenHere (

	val count : Int,
	val lastCheckinExpiredAt : Int,
	val marked : Boolean,
	val unconfirmedCount : Int
)

data class Photos (

	val count : Int,
	val groups : List<String>
)

data class HereNow (

	val count : Int,
	val summary : String,
	val groups : List<HereNowGroups>
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

data class HereNowGroups(
	val type: String,
	val name: String,
	val count: Int,
	val items: List<String>
)
