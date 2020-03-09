package com.umair.helpingout.ui.categories

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.CategoryEntity
import com.umair.helpingout.ui.MainActivity
import com.umair.helpingout.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CategoryListFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CategoryListViewModelFactory by instance()


    private lateinit var viewModel: CategoryListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CategoryListViewModel::class.java)
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).toolbar.animate().translationY(0f).setDuration(600L).start()
        (activity as MainActivity).toolbar.setBackgroundResource(R.color.white)
        (activity as MainActivity).toolbar.setTitleTextColor(resources.getColor(R.color.black))

        //   viewModel = ViewModelProviders.of(this).get(CategoryListViewModel::class.java)
        // TODO: Use the ViewModel

        bindUI()
    }



    private fun bindUI() = launch(Dispatchers.Main) {

        val helpOutDataEntries = viewModel.categoryData.await()


        helpOutDataEntries.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            group_loading.visibility = View.GONE

            /*GlideApp.with(this@ServiceListFragment)
                .load("")*/

            initRecyclerView(it.toHelpOutItems())
        })
    }


    private fun List<CategoryEntity>.toHelpOutItems() : List<CategoryItem>{
        return  this.map {
            CategoryItem(context!!,it)
        }
    }

    private fun initRecyclerView(items : List<CategoryItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CategoryListFragment.context)
            adapter = groupAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        groupAdapter.setOnItemClickListener{item, view ->
            (item as? CategoryItem)?.let {
                val str = resources.getString(R.string.helpful_numbers)
                if(item.categoryItem.catName == str)
                    showHelpfulNumbers(it.categoryItem,view)
                else
                    showCategoryPlaces(it.categoryItem,view)
            }
        }
    }


    private fun showCategoryPlaces(category: CategoryEntity, view: View) {
        val actionDetail = CategoryListFragmentDirections.actionPlaceList(categoryId= category.categoryId, title = category.catName, color = category.color)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun showHelpfulNumbers(category: CategoryEntity, view: View) {
        val actionDetail = CategoryListFragmentDirections.actionHelpfulNumbersFragment( title = category.catName, color = category.color)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun showAboutFragment() {
        val actionAbout = CategoryListFragmentDirections.actionAboutFragment()
        Navigation.findNavController(view!!).navigate(actionAbout)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu_main, menu)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        R.id.action_about -> {
            showAboutFragment()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}
