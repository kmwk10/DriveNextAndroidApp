package com.example.drivenextapp.ui.addCar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton

class AddCarPhotosFragment : Fragment() {

    private val photoViews = mutableListOf<FrameLayout>()
    private val addedPhotos = mutableMapOf<Int, Uri>() // id контейнера → Uri фото
    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>
    private var currentPhotoId: Int = 0 // контейнер, который выбран для добавления фото

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ActivityResultLauncher
        pickPhotoLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val uri: Uri? = data?.data
                if (uri != null) {
                    addedPhotos[currentPhotoId] = uri
                    // Отобразим выбранное фото внутри контейнера
                    val container = photoViews.find { it.id == currentPhotoId }
                    container?.removeAllViews()
                    val imageView = ImageView(requireContext())
                    imageView.setImageURI(uri)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    container?.addView(imageView, FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ))
                    updateNextButtonState()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_car_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)

        btnNext.isEnabled = false

        // Инициализируем контейнеры для фото
        photoViews.add(view.findViewById(R.id.photoContainer1))
        photoViews.add(view.findViewById(R.id.photo2))
        photoViews.add(view.findViewById(R.id.photo3))
        photoViews.add(view.findViewById(R.id.photo4))
        photoViews.add(view.findViewById(R.id.photo5))

        // Обработка кликов на контейнеры
        photoViews.forEach { photoView ->
            photoView.setOnClickListener {
                currentPhotoId = photoView.id
                openGallery()
            }
        }

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_addCarPhotosFragment_to_addCarFragment2)
        }

        btnNext.setOnClickListener {
            if (btnNext.isEnabled) {
                // Здесь можно добавить логику загрузки фото на сервер
                findNavController().navigate(R.id.action_addCarPhotosFragment_to_addCarSuccessFragment)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickPhotoLauncher.launch(intent)
    }

    private fun updateNextButtonState() {
        val btnNext = view?.findViewById<MaterialButton>(R.id.btnNext)
        btnNext?.isEnabled = addedPhotos.isNotEmpty()
    }
}
