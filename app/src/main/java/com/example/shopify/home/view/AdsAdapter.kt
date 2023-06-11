package com.example.shopify.home.view

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.databinding.ItemImgeBinding
import com.example.shopify.home.model.Ads

class AdsAdapter(var ads:ArrayList<Ads>, var viewPager2: ViewPager2):RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {
   private lateinit var context: Context
    private val runnable = Runnable {
        ads.addAll(ads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemImgeBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return AdsViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return ads.size
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.adsBinding.productImg.setImageResource(ads[position].image)
        holder.adsBinding.root.setOnClickListener {
            couponCodeDialog(ads[position].code)

        }
       if (position == ads.size-1){
           viewPager2.post(runnable)
       }

    }
    fun couponCodeDialog(code:String) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.coupon_dialog)
        dialog.findViewById<TextView>(R.id.couponCode).text = code
        dialog.findViewById<Button>(R.id.copy).setOnClickListener {
            var clipboard: ClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip: ClipData = ClipData.newPlainText("coupon", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show()
        }
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
    inner class AdsViewHolder(var adsBinding: ItemImgeBinding) : RecyclerView.ViewHolder(adsBinding.root)
}