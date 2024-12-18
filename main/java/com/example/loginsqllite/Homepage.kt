package com.example.loginsqllite

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
class Homepage : Fragment(R.layout.activity_homepage), UploadRequestBody.UploadCallback {

    private lateinit var imageView: ImageView
    private lateinit var responseTextView: TextView
    private var selectedImageUri: Uri? = null
    lateinit var treatment: TextView

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageView.setImageURI(it)
            selectedImageUri = it
        }
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
    private fun uploadImage() {
        val context = requireContext()

        if (selectedImageUri == null) {
            view?.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context.cacheDir, context.contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("file", file.name, body)

        val desc = null
        Api_Service().uploadImage(
            filePart,
            desc
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { uploadResponse ->
                        //view?.snackbar(uploadResponse.prediction)
                        responseTextView.text = "${response.body()}"

                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    view?.snackbar(errorBody ?: "Unknown error")
                    responseTextView.text = errorBody ?: "Unknown error"
                }
                treatment = view?.findViewById(R.id.treatRecommend)!!
                if(responseTextView.text=="UploadResponse(prediction=Orange: Haunglongbing (Citrus greening))"){
                    treatment.text= "Using Streptomycin and antibiotics can help."
                }
                else if(responseTextView.text=="UploadResponse(prediction=Corn: Cercospora leaf spot, gray leaf spot)"){
                    treatment.text="Products containing chlorothalonil, myclobutanil or " +
                            "thiophanate-methyl are most effective when applied prior to or at " +
                            "the first sign of leaf spots."
                }
                else if(responseTextView.text=="UploadResponse(prediction=Corn: Common_rust_)"){
                    treatment.text= "Products containing mancozeb, pyraclostrobin, " +
                            "pyraclostrobin + metconazole, pyraclostrobin + fluxapyroxad, azoxystrobin + " +
                            "propiconazole, trifloxystrobin + prothioconazole can be used to control the disease."

                }
                else if(responseTextView.text=="UploadResponse(prediction=Cherry: Powdery mildew)"){
                    treatment.text= "Some fungicides that may eradicate a powdery mildew " +
                            "infection are horticultural oils, sulfur, and biological fungicides."
                }
                else if(responseTextView.text=="UploadResponse(prediction=Apple: healthy)"){
                    treatment.text= "Your plant is totally healthy!"
                }
                else if(responseTextView.text=="UploadResponse(prediction=Blueberry: healthy)"){
                    treatment.text= "Your plant is totally healthy!"
                }
                else if(responseTextView.text=="UploadResponse(prediction=Cherry: Healthy)"){
                    treatment.text= "Your plant is totally healthy!"
                }
                else if(responseTextView.text=="UploadResponse(prediction=Potato: Late blight)"){
                    treatment.text= "It is recommended to apply fungicides with a spore-killing effect (fluazinam-containing fungicides, Ranman Top) mainly. "
                }
                else if(responseTextView.text=="UploadResponse(prediction=Potato: Early blight)"){
                    treatment.text= "It is recommended to use Dithane (mancozeb) MZ or you can also use Tattoo C or Acrobat MZ.. "
                }
                else if(responseTextView.text=="UploadResponse(prediction=Pepper (bell): healthy)"){
                    treatment.text= "Your plant is totally healthy!"
                }
                else if(responseTextView.text=="UploadResponse(prediction=Strawberry: healthy"){
                    treatment.text= "Your plant is totally healthy!"
                }
                else if(responseTextView.text=="UploadResponse(prediction=Tomato: healthy"){
                    treatment.text= "Your plant is totally healthy!"
                }

            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                view?.snackbar(t.message!!)
                responseTextView.text = t.message
            }

        }
        )


    }





    private fun openImageChooser() {
        pickImageLauncher.launch("image/*")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_homepage, container, false)
        imageView = view.findViewById(R.id.addImageGallery)
        responseTextView = view.findViewById(R.id.responseTextView)

        imageView.setOnClickListener {
            openImageChooser()
        }

        val button = view.findViewById<Button>(R.id.buttonSubmit)
        button.setOnClickListener {
            uploadImage()
            Thread.sleep(25000)
            recommendTreatment()
        }


        return view
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()
    }

    override fun onProgressUpdate(percentage: Int) {
        // Implement progress update if needed
    }


    private fun recommendTreatment(){

    }
}
