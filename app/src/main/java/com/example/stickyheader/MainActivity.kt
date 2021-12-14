package com.example.stickyheader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stickyheader.adapter.item.ItemModel
import com.example.stickyheader.adapter.item.ItemSectionDecoration
import com.example.stickyheader.adapter.item.TestAdapter

class MainActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }
    private lateinit var adapter: TestAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var itemSectionDecoration: ItemSectionDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initList()

        reload()
    }

    private fun initList(){
        layoutManager = LinearLayoutManager(this)
        adapter = TestAdapter {
            loadMore()
        }
        itemSectionDecoration = ItemSectionDecoration(this){
            adapter.list
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun reload(){
        val list = dummyData(0, 50)
        recyclerView.post{
            adapter.reload(list)
        }
    }

    private fun loadMore(){
        val list = dummyData(adapter.itemCount, 20)

    }

    private fun dummyData(offset: Int, limit: Int): MutableList<ItemModel>{
        val list = mutableListOf<ItemModel>()

        var itemModel: ItemModel
        for(i in offset until offset + limit){
            itemModel = when(i){
                in 0..15 -> {
                    ItemModel("title $i", getDummyDataString("01"))
                }
                in 16..30 -> {
                    ItemModel("title $i", getDummyDataString("02"))
                }
                else -> {
                    ItemModel("title $i", getDummyDataString("03"))
                }
            }
            list.add(itemModel)
        }
        return list
    }

    private fun getDummyDataString(day: String): String {
        return "2021-12-$day"
    }
}