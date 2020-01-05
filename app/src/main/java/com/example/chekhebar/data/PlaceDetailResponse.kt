/* 
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class PlaceEntity (

	val meta : Meta,
	val response : Response
)

data class Meta (

	val code : Int,
	val requestId : String
)

data class Response (

	val venue : Venue
)

data class Venue (

	val id : String,
	val name : String,
	val contact : Contact,
	val location : Location,
	val canonicalUrl : String,
	val categories : List<Categories>,
	val verified : Boolean,
	val stats : Stats,
	val likes : Likes,
	val dislike : Boolean,
	val ok : Boolean,
	val rating : Double,
	val ratingColor : String,
	val ratingSignals : Int,
	val allowMenuUrlEdit : Boolean,
	val beenHere : BeenHere,
	val specials : Specials,
	val photos : Photos,
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

data class Contact (

	val phone : Int,
	val formattedPhone : String
)

data class Location (

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