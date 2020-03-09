package com.umair.helpingout.data.repository

import androidx.lifecycle.LiveData
import com.umair.helpingout.data.db.dao.*
import com.umair.helpingout.data.db.entity.*
import com.umair.helpingout.data.db.preferences.PreferenceProvider
import com.umair.helpingout.data.network.CategoryDataSource
import com.umair.helpingout.data.network.NumberDataSource
import com.umair.helpingout.data.network.PlaceDataSource
import com.umair.helpingout.data.network.response.PlaceListByTagsResponse
import com.umair.helpingout.data.network.response.PlacesListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val MINIMUM_INTERVAL = 1

class DataRepositoryImpl(
    private val placeDao: PlaceDao,
    private val placeDataSource : PlaceDataSource,
    private val numberDataSource: NumberDataSource,
    private val preferenceProvider: PreferenceProvider,
    private val categoryDao : CategoryDao,
    private val placeTagDao: PlaceTagDao,
    private val tagDao: TagDao,
    private val numberDao: NumberDao,
    private val categoryDataSource: CategoryDataSource

) : DataRepository {

    private val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH)

    init {

        /**
         *
         * Place list by category
         *
         */
        placeDataSource.downloadPlaceListByCategory.observeForever{ placesList ->

            if(placesList?.category != null && placesList?.places != null)
            //persist
                persistFetchedPlaceListByCategory(placesList)
        }


        /**
         *
         * Place Detail
         *
         */
        placeDataSource.downloadPlaceDetail.observeForever{ placeDetail ->
            if(placeDetail!=null)
                persistFetchedPlaceDetail(placeDetail)
        }

        numberDataSource.downloadHelpfulNumbers.observeForever{ numbers ->
            if(numbers!=null)
                persistFetchedNumberList(numbers.numberEntities)
        }

        /*helpOutDataSource.downloadAllPlaces.observeForever{ allPlaces ->
            if(allPlaces!=null)
                persistFetchedPlaceList(allPlaces)

        }*/



        /**
         *
         * Places List by Tags
         *
         */
        placeDataSource.downloadPlaceListByTags.observeForever{ placeTags ->
            if(placeTags!=null)
                persistFetchedPlaceTagList(placeTags)
        }


        /**
         *
         * Observer for category list changes
         *
         */
        categoryDataSource.downloadCategoryList.observeForever{ categoryList ->
            if(categoryList!=null)
                //persist
                persistFetchedCategoryList(categoryList)
        }


    }


    private fun PlacesListResponse.toCategoryPlace():List<CategoryPlaceCrossRef>{
        return this.places.map {
            CategoryPlaceCrossRef(this.category, it.placeId)
        }
    }

    override suspend fun getTagList(): LiveData<List<TagEntity>> {
        return withContext(Dispatchers.IO){
            return@withContext tagDao.getAllTags()
        }
    }

    override suspend fun getPlaceListByCategory(categoryId: String): LiveData<CategoryWithPlaces> {
        return withContext(Dispatchers.IO){
            initPlaceListByCategory(categoryId)
            return@withContext categoryDao.getCategoryWithPlaces(categoryId)//getPlaceListByCategory(categoryId)
        }
    }

    private fun persistFetchedPlaceListByCategory(fetchedData : PlacesListResponse){

        GlobalScope.launch(Dispatchers.IO) {
            preferenceProvider.setPlaceListLastUpdatedByCatAt(fetchedData.category,sdf.format(Calendar.getInstance().time))
            placeDao.insertList(fetchedData.places)
            categoryDao.insertCategoryPlace(fetchedData.toCategoryPlace())
        }
    }

    private suspend fun initPlaceListByCategory(categoryId: String){
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMonths(1)))
        val lastUpdatedAt = preferenceProvider.getPlaceListLastUpdatedByCatAt(categoryId)
        if(lastUpdatedAt == null || isFetchNeeded(sdf.parse(lastUpdatedAt))){
            fetchPlaceListByCategory(categoryId)
        }
    }

    private suspend fun fetchPlaceListByCategory(categoryId: String){
        placeDataSource.fetchPlaceListByCategory(categoryId)
    }

    override suspend fun getPlaceDetail(placeId: String): LiveData<PlaceEntity> {
        return withContext(Dispatchers.IO){
            /*val place = helpOutDao.getPlaceDetail(placeId)
            if(place!=null)
                return@withContext place
            else{
                fetchPlaceDetail(placeId)
            }*/
            initPlaceDetail(placeId)
            return@withContext placeDao.getPlaceDetail(placeId)

        }

    }

    private suspend fun initPlaceDetail(placeId: String){
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMonths(1)))
        val lastUpdatedAt = preferenceProvider.getPlaceDetailLastUpdatedAt(placeId)
        if(lastUpdatedAt == null || isFetchNeeded(sdf.parse(lastUpdatedAt))){
            fetchPlaceDetail(placeId)
        }
    }

    private fun persistFetchedPlaceDetail(fetchedData : PlaceEntity){
        fun deleteOldData(placeId: String){
            placeDao.deletePlace(placeId)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldData(fetchedData.placeId)
            placeDao.insert(fetchedData)
            preferenceProvider.setPlaceDetailLastUpdatedAt(fetchedData.placeId,sdf.format(Calendar.getInstance().time))

        }
    }

    private suspend fun fetchPlaceDetail(placeId : String){
        placeDataSource.fetchPlaceDetail(placeId)
    }


    override fun getPlaceListByTags(): LiveData<List<PlaceTagEntity>> {
        return placeTagDao.getPlaceTagList()
    }


    override suspend fun searchPlaceListByTags(filters: List<String>) {
        withContext(Dispatchers.IO) {
            if(isFetchNeededPlaceListByTags(filters))
                fetchPlaceListByTags(filters)
        }
    }




    /**
     *
     * Get Category list from data source
     *
     */

    override suspend fun getCategoryList(): LiveData<List<CategoryEntity>> {
        return withContext(Dispatchers.IO){
            initCategoriesData()
            return@withContext categoryDao.getAllCategories()
        }
    }

    override suspend fun getNumbers(): LiveData<List<NumberEntity>> {
        return withContext(Dispatchers.IO){
            initNumbersData()
            return@withContext numberDao.getAllNumbers()
        }
    }

    override suspend fun updateTagSelection(tagEntity: TagEntity) {
        withContext(Dispatchers.IO){
            tagDao.toggleSelectTag(tagEntity.id, !tagEntity.isSelected)
        }
    }


    /**
     *
     * Category
     *
     * Save updated Category List in database
     *
     */
    private fun persistFetchedCategoryList(fetchedData : List<CategoryEntity>){
        fun deleteOldData(){
            categoryDao.deleteAll()
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldData()
            categoryDao.insert(fetchedData)
            preferenceProvider.setCategoriesLastUpdatedAt(sdf.format(Calendar.getInstance().time))
        }
    }


    /**
     *
     * Check if update required
     * true: fetch new data from server
     * false: fetch data from database
     *
     */
    private suspend fun initCategoriesData(){
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMonths(1)))
        val lastUpdatedAt = preferenceProvider.getCategoriesLastUpdatedAt()
        if(lastUpdatedAt == null || isFetchNeeded(sdf.parse(lastUpdatedAt))){
            fetchCategoryList()
        }
    }




    /**
     *
     * Check if update required
     * true: fetch new data from server
     * false: fetch data from database
     *
     */
    private suspend fun initNumbersData(){
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMonths(1)))
        val lastUpdatedAt = preferenceProvider.getNumbersLastUpdatedAt()
        if(lastUpdatedAt == null || isFetchNeeded(sdf.parse(lastUpdatedAt))){
            fetchNumbersList()
        }
    }


    /**
     *
     * Fetch category list from server
     *
     */
    private suspend fun fetchNumbersList(){
        numberDataSource.fetchAllNumbers()
    }

    /**
     *
     * Fetch category list from server
     *
     */
    private suspend fun fetchCategoryList(){
        categoryDataSource.fetchCategoryList()
    }


    /**
     *
     * Fetch new data if last update was MINIMUM_INTERVAL month ago
     *
     */
    private fun isFetchNeeded(lasttFetchTime : Date?) : Boolean{
        val oneMonthAgo = Calendar.getInstance()
        oneMonthAgo.add(Calendar.MONTH, -MINIMUM_INTERVAL)
        return lasttFetchTime!!.before(oneMonthAgo.time)//isBefore(oneMonthAgo)
    }









    /**
     *
     * Init place list by tags
     *
     */
    private  fun isFetchNeededPlaceListByTags(filter: List<String>) : Boolean{
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMonths(1)))
        val lastUpdatedAt = preferenceProvider.getPlaceByTagLastUpdatedAt()
        val oldTags = preferenceProvider.getTagsList()
        return lastUpdatedAt == null || oldTags == null || !oldTags.containsAll(filter.toSet()) || isFetchNeeded(sdf.parse(lastUpdatedAt))

    }




    /**
     *
     * Fetch place by tags list from server
     *
     */
    private suspend fun fetchPlaceListByTags(tags : List<String>){
        placeDataSource.fetchPlaceListByTags(tags)
    }



    /**
     *
     * Category
     *
     * Save updated Category List in database
     *
     */
    private fun persistFetchedPlaceTagList(fetchedData : PlaceListByTagsResponse){
        fun deleteOldData(){
            placeTagDao.deleteAll()
        }
        GlobalScope.launch(Dispatchers.IO) {
            if(fetchedData.clearOld)
                deleteOldData()

            placeTagDao.insertList(fetchedData.places)
            preferenceProvider.setTagsList(fetchedData.tags.toSet())
            preferenceProvider.setLastPage(fetchedData.page)
        }
    }




    /**
     *
     * Helpful Numbers
     *
     * Save updated Category List in database
     *
     */
    private fun persistFetchedNumberList(fetchedData : List<NumberEntity>){

        GlobalScope.launch(Dispatchers.IO) {
            numberDao.insertAll(fetchedData)
        }
    }


}