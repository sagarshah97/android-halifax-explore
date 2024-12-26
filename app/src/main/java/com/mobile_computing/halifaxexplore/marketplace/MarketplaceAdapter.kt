package com.mobile_computing.halifaxexplore.marketplace

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile_computing.halifaxexplore.R

class MarketplaceAdapter(private val items: List<MarketplaceItem>) :
    RecyclerView.Adapter<MarketplaceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val itemDescriptionTextView: TextView = itemView.findViewById(R.id.itemDescriptionTextView)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        val sellerEmailTextView: TextView = itemView.findViewById(R.id.sellerEmailTextView)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = items[position]
                showItemDetailsDialog(item)
            }
        }

        private fun showItemDetailsDialog(item: MarketplaceItem) {
            val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_marketplace_item_details, null)

            val dialogItemImageView: ImageView = dialogView.findViewById(R.id.dialogItemImageView)
            val dialogItemNameTextView: TextView = dialogView.findViewById(R.id.dialogItemNameTextView)
            val dialogItemDescriptionTextView: TextView = dialogView.findViewById(R.id.dialogItemDescriptionTextView)
            val dialogItemPriceTextView: TextView = dialogView.findViewById(R.id.dialogItemPriceTextView)
            val dialogSellerEmailTextView: TextView = dialogView.findViewById(R.id.dialogSellerEmailTextView)
            val contactSellerButton: Button = dialogView.findViewById(R.id.contactSellerButton)

            Glide.with(dialogItemImageView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.default_item_image)
                .into(dialogItemImageView)

            dialogItemNameTextView.text = item.itemName
            dialogItemDescriptionTextView.text = item.itemDescription
            dialogItemPriceTextView.text = "$" + item.itemPrice
            dialogSellerEmailTextView.text = item.sellerEmail

            val email = item.sellerEmail

            contactSellerButton.setOnClickListener {
                sendEmail(email)
            }

            AlertDialog.Builder(itemView.context)
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        private fun sendEmail(email: String) {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$email")
            itemView.context.startActivity(emailIntent)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marketplace, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemImageView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.default_item_image)
            .into(holder.itemImageView)

        holder.itemNameTextView.text = item.itemName
        holder.itemDescriptionTextView.text = item.itemDescription
        holder.itemPriceTextView.text = "$"+item.itemPrice
        holder.sellerEmailTextView.text = item.sellerEmail
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

