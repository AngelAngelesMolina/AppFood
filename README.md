
# AppFood

This is a food app where you can see the ready meals and put them as favorites, in addition to seeing the process to create the meals and also a video on youtube.





## API Reference
- [Link](https://www.themealdb.com/api.php)



## Arquitecture

- Model ViewModel View (MVVM)
  

## Libraries and technologies used
- Navigation component : one activity contains multiple fragments instead of creating multiple activites.
- Retrofit : making HTTP connection with the rest API and convert meal json file to Kotlin/Java object.
- Room : Save meals in local database.
- MVVM & LiveData : Saperate logic code from views and save the state in case the screen configuration changes.
- Coroutines : do some code in the background.
- View binding : instead of inflating views manually view binding will take care of that.
- Glide : Catch images and load them in imageView.
 
 ## Screenshots
![Vista principal](https://github.com/AngelAngelesMolina/AppFood/blob/main/app/homeFragment.png?raw=true)
  ![Vista miembros](https://github.com/AngelAngelesMolina/AppFood/blob/main/app/FavoritesFragment.png?raw=true)
  ![Vista miembros](https://github.com/AngelAngelesMolina/AppFood/blob/main/app/categoriesFragment.png?raw=true)
  ![Vista miembros](https://github.com/AngelAngelesMolina/AppFood/blob/main/app/items_Category.png?raw=true)
  ![Vista miembros](https://github.com/AngelAngelesMolina/AppFood/blob/main/app/detailMeal.png?raw=true)
