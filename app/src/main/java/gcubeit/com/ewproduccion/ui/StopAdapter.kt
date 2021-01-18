package gcubeit.com.ewproduccion.ui

import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gcubeit.com.ewproduccion.R
import gcubeit.com.ewproduccion.model.Stop
import kotlinx.android.synthetic.main.item_stop.view.*

class StopAdapter
    : RecyclerView.Adapter<StopAdapter.ViewHolder>() {

    var stops = ArrayList<Stop>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stop: Stop) = with(itemView) {
            tvStopId.text = context.getString(R.string.item_stop_id, stop.id)
            tvCodeDescription.text = context.getString(R.string.item_stop_description, stop.code.code, stop.code.description)
            tvStopType.text = stop.code.type
            tvMachine.text = stop.machine.machine_name

            if(stop.product != null) {
                tvProduct.text = stop.product.product_name
            } else {
                tvProduct.text = ""
                tvProduct.visibility = View.GONE
            }

            if(stop.color != null) {
                tvColor.text = stop.color.name
                tvStopId.setBackgroundColor(Color.parseColor(stop.color.hex_code))
            } else {
                tvColor.text = ""
                tvColor.visibility = View.GONE
            }

            tvStopDateStart.text = context.getString(R.string.item_stop_date_start, stop.stopDateStart)
            tvStopTimeStart.text = context.getString(R.string.item_stop_time_start, stop.stopTimeStart)
            tvStopDateEnd.text = context.getString(R.string.item_stop_date_end, stop.stopDateEnd)
            tvStopTimeEnd.text = context.getString(R.string.item_stop_time_end, stop.stopTimeEnd)
        }
    }

    // Inflates XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stop, parent, false)
        )
    }

    // Binds data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stop = stops[position]
        holder.bind(stop)
    }

    // Number of elements
    override fun getItemCount() = stops.size
}