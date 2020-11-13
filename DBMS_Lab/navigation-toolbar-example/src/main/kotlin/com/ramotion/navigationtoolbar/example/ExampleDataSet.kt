package com.ramotion.navigationtoolbar.example

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

    fun getItemData(pos: Int): ItemData
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

    internal val headerDataSet = object : HeaderDataSet {
        override fun getItemData(pos: Int) =
                HeaderDataSet.ItemData(
                    gradient = headerGradients[pos % headerGradients.size],
                    background = headerBackgrounds[pos % headerBackgrounds.size],
                    title = headerTitles[pos % headerTitles.size])
    }

    internal val viewPagerDataSet = object : ViewPagerDataSet {
        val pageItemCount = 5

        override fun getPageData(page: Int) = object : PageDataSet {
            override val secondItemImage = headerDataSet.getItemData(page).background

            override fun getItemData(pos: Int): PageDataSet.ItemData {
                val localPos = page * pageItemCount + pos
                return PageDataSet.ItemData(
                        //avatar = avatars[localPos % avatars.size],
                        requests = requests[localPos % requests.size],
                        price = prices[localPos % prices.size])
            }
        }
    }

}