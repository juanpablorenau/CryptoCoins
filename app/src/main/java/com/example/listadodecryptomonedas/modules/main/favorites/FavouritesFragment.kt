package com.example.listadodecryptomonedas.modules.main.favorites

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.adapters.CryptoRecyclerAdapter
import com.example.listadodecryptomonedas.data.model.CryptoCoin
import com.example.listadodecryptomonedas.databinding.FragmentFavoritesBinding
import com.example.listadodecryptomonedas.helper.SwipeGesture
import com.example.listadodecryptomonedas.helper.toDp
import kotlin.math.roundToInt

class FavouritesFragment : Fragment(), CryptoRecyclerAdapter.RecyclerViewInterface {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    private lateinit var cryptoRecyclerAdapter: CryptoRecyclerAdapter
    private lateinit var swipeHelper: ItemTouchHelper

    companion object {
        private const val DRAWABLE_HEIGHT_MARGIN = 12
        private const val WIDTH_DIVISION = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        binding = FragmentFavoritesBinding.bind(view)
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initialize()

        return view
    }

    private fun initialize() {
        viewModel.getFavorites()
        initObservers()
        initRecyclerAdapter()
        initSwipe()
        refreshListener()
    }

    private fun initObservers() {
        viewModel.favoriteCryptoCoins.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNotEmpty()) {
                favorites.sortBy { it.id }
                cryptoRecyclerAdapter.data = favorites.distinctBy { it.id }
            }
        }
    }

    private fun initRecyclerAdapter() {
        cryptoRecyclerAdapter = CryptoRecyclerAdapter(this, this)
        cryptoRecyclerAdapter.data = viewModel.favoriteCryptoCoins.value ?: listOf()
        binding.recycler.adapter = cryptoRecyclerAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun initSwipe() {
        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
                val cryptoCoin =
                    viewModel.favoriteCryptoCoins.value?.get(viewHolder.adapterPosition)
                viewModel.favoriteCryptoCoins.value?.removeAt(viewHolder.adapterPosition)
                viewModel.favoriteCryptoCoins.value = viewModel.favoriteCryptoCoins.value
                cryptoRecyclerAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                cryptoCoin?.id?.let { viewModel.deleteId(it) }
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val deleteIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_outline)

                setSwipeMargins(viewHolder, canvas, dX)
                setIconMargins(viewHolder, deleteIcon)
                deleteIcon?.draw(canvas)

                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        swipeHelper = ItemTouchHelper(swipeGesture)
        swipeHelper.attachToRecyclerView(binding.recycler)
    }

    private fun setSwipeMargins(viewHolder: RecyclerView.ViewHolder, canvas: Canvas, dX: Float) {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt().toDp()
        val paint = Paint()
        when {
            kotlin.math.abs(dX) < width / WIDTH_DIVISION -> {
                paint.color = resources.getColor(R.color.dark_gray, null)
            }
            dX > width / WIDTH_DIVISION -> {
                paint.color = resources.getColor(R.color.red, null)
            }
        }
        canvas.drawRect(
            viewHolder.itemView.left.toFloat(),
            viewHolder.itemView.top.toFloat(),
            viewHolder.itemView.right.toFloat(),
            viewHolder.itemView.bottom.toFloat(),
            paint
        )
    }

    private fun setIconMargins(viewHolder: RecyclerView.ViewHolder, deleteIcon: Drawable?) {
        val textMargin =
            resources.getDimension(androidx.constraintlayout.widget.R.dimen.abc_action_bar_content_inset_material)
                .roundToInt()

        deleteIcon?.bounds = Rect(
            textMargin,
            viewHolder.itemView.top + DRAWABLE_HEIGHT_MARGIN.toDp(),
            textMargin + (deleteIcon?.intrinsicWidth ?: 0),
            viewHolder.itemView.top + (
                deleteIcon?.intrinsicHeight
                    ?: 0
                ) + DRAWABLE_HEIGHT_MARGIN.toDp()
        )
    }

    private fun refreshListener() {
        binding.swipe.setOnRefreshListener {
            viewModel.getFavorites()
            binding.swipe.isRefreshing = false
        }
    }

    override fun onCryptoClickListener(id: String?) {
    }

    override fun onCryptoLongClickListener(cryptoCoin: CryptoCoin, position: Int) {
    }
}
