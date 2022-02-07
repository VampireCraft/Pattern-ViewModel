package dt.prod.patternvm.listProblem.domain

import dt.prod.patternvm.core.model.ResponseWrapper
import dt.prod.patternvm.listProblem.models.ListItemModel
import retrofit2.http.*

interface EventApi {
    @POST("/rzd/Api.php")
    suspend fun editProblem(
        @Query("apicall") apicall: String,
        @Query("problem_id") id_problem:String,
        @Query("room_num") tag:String,
        @Query("definition") description: String,
        @Query("adress_num") typeUsers:String,
        @Query("title") name:String,
        @Query("data_remove") timeRemove: String,
        @Query("date_accept") timeAccept:String,
        @Query("date_over") timeOver:String,
        @Query("image_problem") image:String

    ): ResponseWrapper<String>

    @GET("/rzd/Api.php")
    suspend fun getListProblem(
        @Query("apicall") apicall: String,
        @Query("adress_num") adress_num: String
    ): ResponseWrapper<List<ListItemModel>>

}