# MemorablePlaces
An app that keeps track of favorite/memorable places you visited or would like to visit in the future.


# Learnings
- Custom styled map
- SharedPreferences to store user's Memorable Place info
- Geocoding latlng -> address
- Gson library to parse the memorableplace data to json string
- IntentService, to Geocode then write in SharedPreferences 
- SharedPreferences listener to modify MainActivitiy's List accordingly
- AsyncTask to re-fetch the listview's data

# Screenshots
<img src="/images/MainActivity.png" width="250"/>        <img src="/images/MapActivity.png" width="250"/>
