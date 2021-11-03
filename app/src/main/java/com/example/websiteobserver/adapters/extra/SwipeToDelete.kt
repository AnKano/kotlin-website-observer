package com.example.websiteobserver.adapters.extra

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.websiteobserver.R
import com.example.websiteobserver.WebsiteObserverApplication
import com.example.websiteobserver.adapters.WebsiteListAdapter
import com.example.websiteobserver.database.WebsiteRoomDatabase
import kotlinx.coroutines.launch


class SwipeToDelete(private val context: Context, private val adapter: WebsiteListAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView

        val background = ColorDrawable(Color.parseColor("#e8110c"))
        when {
            dX < 0 -> {
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)

        val canvasGap = 25
        val fontSize = 64F
        c.drawText("DELETE",
            itemView.right + dX + canvasGap,
            itemView.bottom + dY - canvasGap,
            Paint().apply {
                color = Color.WHITE
                textSize = fontSize
                typeface = ResourcesCompat.getFont(recyclerView.context, R.font.inter_extrabold)
            })
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        val scope = (context.applicationContext as WebsiteObserverApplication).applicationScope
        scope.launch {
            val database: WebsiteRoomDatabase =
                (context.applicationContext as WebsiteObserverApplication).database
            database.dao().deleteEntry(adapter.currentList[position])
        }
    }

}