package com.sample.vkoelassign.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.vkoelassign.databinding.ItemMovieBinding

class MovieAdapter :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private lateinit var binding: ItemMovieBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /*       init {
                   itemBinding.itemCard.setOnClickListener {
                       itemView.isEnabled = false
                       Handler().postDelayed({
                           itemView.isEnabled = true
                           data?.get(adapterPosition).let {
                               openNewsDetailActivity(itemView, adapterPosition)
                               *//*val action = HeadLineFragmentDirections.actionHeadLineDetail()
                        action.articleDetailModel = it
                        Navigation.findNavController(itemBinding.root).navigate(action)*//*
                    }
                }, 100)
            }
        }

        fun bindItem(articleDetailModel: ArticleDetailModel) {
            //itemBinding.titleTxtView.text = articleDetailModel.title
            Utils.showFadeInAnimOnText(
                itemView.context,
                itemBinding.titleTxtView,
                articleDetailModel.title
            )

            //itemBinding.siteTxtView.text = articleDetailModel.source?.sourceModelName
            Utils.showBounceAnimOnText(
                itemView.context,
                itemBinding.siteTxtView,
                articleDetailModel.source?.sourceModelName!!
            )

            //itemBinding.dateTxtView.text = Utils.getDatefrom(articleDetailModel.publishedAt!!)
            Utils.showBounceAnimOnText(
                itemView.context,
                itemBinding.dateTxtView,
                Utils.getDatefrom(articleDetailModel.publishedAt!!)!!
            )

            Handler().postDelayed({
                if (!articleDetailModel.urlToImage.isNullOrEmpty()) {
                    Utils.setImage(itemBinding.proImageView, articleDetailModel.urlToImage)
                } else {
                    Log.d(
                        "DEBUG",
                        "NewsAdapter articleDetailModel.urlToImage is ${articleDetailModel.urlToImage}"
                    )
                    val dummyUrl =
                        "https://ichef.bbci.co.uk/news/1024/branded_news/D268/production/_118046835_screenshot2021-04-13at22.37.06.png"
                    Utils.setImage(itemBinding.proImageView, dummyUrl)
                }
            }, 100)

        }*/

    }


}