package com.example.myapplication.support

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*


class CalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val rvDays: RecyclerView by lazy { findViewById(R.id.rvDays) }
    private val rvMonths: RecyclerView by lazy { findViewById(R.id.rvMonths) }

    var onDateChangedCallback: DateChangeListener? = null

    val selectedDate: Date?
        get() {
            return (rvDays.adapter as DaysAdapter).selectedDate
        }


    init {
        context.theme?.let { theme ->
            val attr = theme.obtainStyledAttributes(attributeSet, R.styleable.CalendarView, 0, 0)
            val days = attr.getInteger(R.styleable.CalendarView_daysCount, 30)

            View.inflate(context, R.layout.calendar_view, this)
            rvDays.adapter = DaysAdapter(generateDays(days)) {
                onDateChangedCallback?.onDateChanged(it)
            }
            rvMonths.adapter = MonthsAdapter(generateMonths())
            attr.recycle()
        }
    }

    private fun generateMonths(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val list = arrayListOf<Date>()

        for (i in 0..30) {
            list.add(calendar.time)
            calendar.add(Calendar.MONTH, 1)
        }
        return list
    }

    private fun generateDays(daysCount: Int): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val list = arrayListOf<Date>()

        for (i in 0 until daysCount) {
            list.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    class DaysAdapter(
        private val items: List<Date>,
        private val onDateChangedCallback: (Date) -> Unit
    ) : RecyclerView.Adapter<DayViewHolder>() {

        var selectedDate: Date? = null
            private set

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            return DayViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_day_list_item, parent, false), ::onItemClick

            )
        }

        private fun onItemClick(pos: Int) {
            val prevPos = items.indexOf(selectedDate)
            selectedDate = items[pos]
            onDateChangedCallback(items[pos])
            notifyItemChanged(prevPos)
            notifyItemChanged(pos)

        }

        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            holder.bind(items[position], selectedDate == items[position])
        }

        override fun getItemCount(): Int = items.size

    }

    class DayViewHolder(itemView: View, onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val tvDay = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvWeekDay = itemView.findViewById<TextView>(R.id.tvWeekday)

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)

            }
        }

        fun bind(date: Date, selected: Boolean) {
            tvDay.text = monthDayFormatter.format(date)
            tvWeekDay.text = weekDayFormatter.format(date)
            val nowDay = monthDayFormatter.format(Date())
            itemView.setBackgroundResource(
                if (tvDay.text == nowDay) R.drawable.start_day_background else 0
            )
            itemView.setBackgroundResource(
                if (selected) {
                    R.drawable.selected_day_background

                } else 0
            )

//            val colorText = if (selected) Color.WHITE else Color.BLACK

            tvWeekDay.setTextColor(Color.WHITE)
            tvDay.setTextColor(Color.WHITE)

        }

        companion object {
            val monthDayFormatter = SimpleDateFormat("dd", Locale.getDefault())
            val weekDayFormatter = SimpleDateFormat("EE", Locale.getDefault())
        }

    }

    class MonthsAdapter(private val items: List<Date>) : RecyclerView.Adapter<MonthsViewHolder>() {

        private var selectedDate: Date = Date()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthsViewHolder {
            return MonthsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_months_list_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MonthsViewHolder, position: Int) {
            holder.bind(items[position], selectedDate == items[position])
        }

        override fun getItemCount(): Int = items.size

    }

    class MonthsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvMonth = itemView.findViewById<TextView>(R.id.tvMonths)

        fun bind(date: Date, selected: Boolean) {
            tvMonth.text = monthsFormatter.format(date)
            tvMonth.setTextColor(Color.WHITE)

        }

        companion object {
            val monthsFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        }
    }

    interface DateChangeListener {
        fun onDateChanged(date: Date)
    }
}