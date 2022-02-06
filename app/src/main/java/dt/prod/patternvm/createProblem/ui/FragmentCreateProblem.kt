 package dt.prod.patternvm.createProblem.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.tbruyelle.rxpermissions2.RxPermissions
import dt.prod.patternvm.core.ui.BaseFragment
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import dt.prod.patternvm.R
import dt.prod.patternvm.createProblem.data.CreateProblemNavigatorImpl
import dt.prod.patternvm.createProblem.models.CreateProblemViewModel
import dt.prod.patternvm.databinding.FragmentCreateProblemBinding

class FragmentCreateProblem : BaseFragment() {
    private lateinit var binding: FragmentCreateProblemBinding
    private lateinit var viewModel: CreateProblemViewModel
    private lateinit var easyImage: EasyImage
    private lateinit var rxPermissions : RxPermissions
    private lateinit var typeCreateEvent: CreateProblemTypeEnum

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        easyImage = EasyImage.Builder(requireContext())
            .setCopyImagesToPublicGalleryFolder(false) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
            .setFolderName("EasyImage sample") // Allow multiple picking
            .allowMultiple(true)
            .build()

        rxPermissions = RxPermissions(this)
        rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe {

            }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProblemBinding.inflate(inflater, container, false)
        //todo создать цепочку от описания, либо обнулять данные после создания
        viewModel = ViewModelProvider(requireActivity())[CreateProblemViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator =
            CreateProblemNavigatorImpl(parentFragmentManager, binding.clRoot.id)
        configureView()
        //paintCameraIcon()
    }

    private fun configureView() {

        binding.clGallery.setOnClickListener {
            typeCreateEvent = CreateProblemTypeEnum.EVENT_GALLERY
            rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) {
                        easyImage.openGallery(this)
                    }
                }
        }

        binding.clCamera.setOnClickListener {
            typeCreateEvent = CreateProblemTypeEnum.EVENT_CAMERA
            rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) {
                        easyImage.openCameraForImage(this)
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            requireActivity(),
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    lifecycleScope.launch {
                        when (typeCreateEvent){
                            CreateProblemTypeEnum.EVENT_CAMERA->{
                                val compressedImageFile = Compressor.compress(requireContext(), imageFiles[0].file){
                                    resolution(1280, 720)
                                    quality(80)
                                    format(Bitmap.CompressFormat.JPEG)
                                }
                                viewModel.navigator?.openCameraScreen(compressedImageFile.absolutePath)
                            }
                            CreateProblemTypeEnum.EVENT_GALLERY->{
                                val compressedImageFile = Compressor.compress(requireContext(), imageFiles[0].file){
                                    resolution(1280, 720)
                                    quality(80)
                                    format(Bitmap.CompressFormat.JPEG)
                                }
                                viewModel.navigator?.openGalleryScreen(compressedImageFile.absolutePath)
                            }
                            else -> {}
                        }

                    }
                }

                override fun onImagePickerError(
                    @NonNull error: Throwable,
                    @NonNull source: MediaSource
                ) {
                    error.printStackTrace()
                }

                override fun onCanceled(@NonNull source: MediaSource) {

                }
            })
    }


    private fun paintCameraIcon() {
        binding.ivCamera.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.sunset_orange
            )
        )
    }


    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun showError(error: String) {
        TODO("Not yet implemented")
    }
}