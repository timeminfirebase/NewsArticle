# NewsArticle

Use Hacker News API to fetch and display news stories using rerofit with Kotlin in Online or Offline mode application.

Getting started
Hacker News API has been developed in partnership with Firebase. Use Firebase API reference or their libraries to further expand your project.
and also store data in SharedPreferences with Kotlin Language.

Get to know your API
Here's some key information that will come in handy when working with the Hacker News API.

URI and versioning
At the moment of writing there's only one version of the API, v0.

Use it with the following URI:

 https://hacker-news.firebaseio.com   
Resource encoding
Requests and responses from the API, including errors, are encoded as JSON.

Error handling
If a required field is omitted or altered in a request, the API will throw an error. Optional fields, if omitted or altered, will be silently ignored by the API.

GET a story
Stories known also as items are defined by their unique IDs which are required. Refer to the Hacker News API for more details on the item object.

URL

https://hacker-news.firebaseio.com/v0/item/:id
Name	Required	Type	Description
id	YES	integer	The id of the requested item
Sample request

curl -X GET https://hacker-news.firebaseio.com/v0/item/24189341 
Sample response

200 OK

{
"by": "atarian",
"descendants": 354,
"id": 24189341,
"kids": [
    24190694,
    24190396,
    24192377,
    24190782,
    24190063,
    24190065
],
"score": 981,
"time": 1597684749,
"title": "I fear App Review is getting too powerful (2015) [pdf]",
"type": "story",
"url": "https://judiciary.house.gov/uploadedfiles/015127.pdf"
}

GET top stories
Get up to 500 top and new stories.

URL

  https://hacker-news.firebaseio.com/v0/beststories
Sample request

curl -X GET https://hacker-news.firebaseio.com/v0/beststories
Sample response

200 OK

[
24201306,
24208958,
24189341,
24193278,
24198329,
24217116,
24190556,
24198228,
24196131,
24199424,
24198172,
24189153  
]

Know about SharedPreferences

SharedPreferences is local storage in android which is used to store data in the form of key and value pairs within the android devices.
Add the below dependency in the dependencies section.

implementation 'com.google.code.gson:gson:2.8.5'

How do I save a list object to SharedPreferences?

I want to save this list to shared preferences.You may do it using Gson as below:

Download List<Product> from webservice.
Convert the List into Json String using new Gson(). toJson(medicineList, new TypeToken<List<Product>>(){}. getType())
Save the converted string into SharePreferences as you do normally.

Also geting stories list added filter and search option.
