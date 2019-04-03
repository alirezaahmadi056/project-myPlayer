package application.ahmadi.myplayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CategoryRecyclerAdapter(private val context: Context, private val categories: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<CategoryRecyclerAdapter.Holder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val categoryView = LayoutInflater.from(context).inflate(R.layout.list_temp, parent, false)
        return Holder(categoryView)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindCategory(categories[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val txtTitle = itemView?.findViewById<TextView>(R.id.txtNameSound)

        fun bindCategory(category: HashMap<String, String>, context: Context) {

            txtTitle?.text = category["title"]

        }

    }

}