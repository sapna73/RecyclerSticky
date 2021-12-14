package com.example.stickyheader.adapter.item

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemSectionDecoration(private val context: Context,
private val getItemList: () -> MutableList<ItemModel>): RecyclerView.ItemDecoration() {

    private val dividerHeight = dipToPx(context, 0.8f)
    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.parseColor("#ff0000")
    }

    private val sectionItemWidth: Int by lazy {
        getScreenWidth(context)
    }

    private val sectionItemHeight: Int by lazy{
        dipToPx(context, 30f)
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager

        if (layoutManager!is LinearLayoutManager){
            return
        }

        if(LinearLayoutManager.VERTICAL != layoutManager.orientation){
            return
        }


        val list = getItemList()
        if (list.isEmpty()){
            return
        }
        val position = parent.getChildAdapterPosition(view)
        if(0 == position){
            outRect.top = sectionItemHeight
            return
        }
        val currentModel = getItemList()[position]
        val previousModel = getItemList()[position - 1]

        if(currentModel.date != previousModel.date){
            outRect.top = sectionItemHeight
        }else{
            outRect.top = dividerHeight
        }
    }

    private fun drawDivider(canvas: Canvas, childView: View){
        canvas.drawRect(
            0f, //left
            (childView.top - dividerHeight).toFloat(),//top
            childView.right.toFloat(),//right
            childView.top.toFloat(),//bottom
            dividerPaint
        )
    }

    private fun drawSectionView(canvas: Canvas, text: String, top: Int){
        val view = SectionViewHolder(context)
        view.setDate(text)

        val bitmap = getViewGroupBitmap(view)
        val bitmapCanvas = Canvas(bitmap)
        view.draw(bitmapCanvas)

        canvas.drawBitmap(bitmap, 0f, top.toFloat(), null)
    }

    private fun getViewGroupBitmap(viewGroup: ViewGroup): Bitmap{
        val layoutParams = ViewGroup.LayoutParams(sectionItemWidth, sectionItemHeight)
        viewGroup.layoutParams = layoutParams

        viewGroup.measure(
            View.MeasureSpec.makeMeasureSpec(sectionItemWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(sectionItemHeight, View.MeasureSpec.EXACTLY)
        )
        viewGroup.layout(0, 0, sectionItemWidth, sectionItemHeight)
        val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        viewGroup.draw(canvas)
        return bitmap
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount){
            val childView: View = parent.getChildAt(i)
            val position: Int = parent.getChildAdapterPosition(childView)
            val itemModel = getItemList()[position]
            if(getItemList().isNotEmpty() &&
                (0 == position || itemModel.date != getItemList()[position - 1].date)){
                val top = childView.top - sectionItemHeight
                drawSectionView(c, itemModel.date, top)
            }else{
                drawDivider(c, childView)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val list = getItemList()
        if(list.isEmpty()){
            return
        }
        val childCount = parent.childCount
        if(childCount == 0){
            return
        }
        val firstView = parent.getChildAt(0)

        val position = parent.getChildAdapterPosition(firstView)
        val text = list[position].date
        val itemModel = list[position]

        val condition = itemModel.date != list[position + 1].date

        drawSectionView(c, text, if (firstView.bottom <= sectionItemHeight && condition){
            firstView.bottom - sectionItemHeight
        }else{
            0
        })
    }

    private fun dipToPx(context: Context, dipValue: Float): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

    private fun getScreenWidth(context: Context): Int{
        val outMetrics = DisplayMetrics()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val display = context.display
            display?.getMetrics(outMetrics)
        }else{
            val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
        return outMetrics.widthPixels
    }
}