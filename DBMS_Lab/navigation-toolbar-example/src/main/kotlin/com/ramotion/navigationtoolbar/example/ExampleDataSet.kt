package com.ramotion.navigationtoolbar.example

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface HeaderDataSet {
    data class ItemData(val gradient: Int,
                        val background: Int,
                        val title: String)

    fun getItemData(pos: Int): ItemData
}

interface PageDataSet {

    data class ItemData(//val avatar: Int,
                       val requests: String,
                       val price: String)

    val secondItemImage: Int

    fun getItemData(pos: Int, page: Int): ItemData
}

interface ViewPagerDataSet {
    fun getPageData(page: Int): PageDataSet
}

class ExampleDataSet {
    private val headerBackgrounds = intArrayOf(R.drawable.card_1_background, R.drawable.card_2_background, R.drawable.card_3_background, R.drawable.card_4_background, R.drawable.card_3_background).toTypedArray()
    private val headerGradients = intArrayOf(R.drawable.card_1_gradient, R.drawable.card_2_gradient, R.drawable.card_3_gradient, R.drawable.card_4_gradient, R.drawable.card_1_gradient).toTypedArray()
    private val headerTitles = arrayOf("STORY", "POEM", "ALL", "TOP RATED","MOST RECENT")

    private val requests = arrayOf("3","4","5","6","1","2","3","4","5")
    //private val avatars = intArrayOf(R.drawable.aaron_bradley, R.drawable.barry_allen, R.drawable.bella_holmes, R.drawable.caroline_shaw, R.drawable.connor_graham, R.drawable.deann_hunt, R.drawable.ella_cole, R.drawable.jayden_shaw, R.drawable.jerry_carrol, R.drawable.lena_lucas, R.drawable.leonrd_kim, R.drawable.marc_baker, R.drawable.marjorie_ellis, R.drawable.mattew_jordan, R.drawable.ross_rodriguez, R.drawable.tina_caldwell, R.drawable.wallace_sutton)
    private val prices = arrayOf("12","23","34","45","56")
    private val db = null
    private var length=0
    data class pub(val author: String,
                        val name: String,
                        val type: String)

     suspend fun func(page: Int, pos: Int): PageDataSet.ItemData? {
        var x = 0
        var ans: PageDataSet.ItemData? = null
        val db = Firebase.firestore
        val info = db.collection("publication").document("01")
        val p = pub(author = "wow", name = "idk", type = "yu")
        GlobalScope.launch {
            info.set(p).await()
        }
        withContext(Dispatchers.Main) {

            ans = PageDataSet.ItemData(
                    //avatar = avatars[localPos % avatars.size],
                    requests = requests[pos % requests.size],
                    price = prices[pos % prices.size])
        }
        return ans
    }


    internal val headerDataSet = object : HeaderDataSet {
        override fun getItemData(pos: Int) =
                HeaderDataSet.ItemData(
                    gradient = headerGradients[pos % headerGradients.size],
                    background = headerBackgrounds[pos % headerBackgrounds.size],
                    title = headerTitles[pos % headerTitles.size])
    }



    internal val viewPagerDataSet = object : ViewPagerDataSet {
        val pageItemCount = length
        val db = Firebase.firestore

        override fun getPageData(page: Int) = object : PageDataSet {

            override val secondItemImage = headerDataSet.getItemData(page).background
            override fun getItemData(pos: Int, page: Int): PageDataSet.ItemData {
                return PageDataSet.ItemData(
                        //avatar = avatars[localPos % avatars.size],
                        requests = requests[pos % requests.size],
                        price = prices[pos % prices.size])
            }

            }
        }
    }