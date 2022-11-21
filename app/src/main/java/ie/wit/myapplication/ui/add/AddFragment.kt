package ie.wit.myapplication.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.FragmentAddBinding
import ie.wit.myapplication.main.MainApp
import ie.wit.myapplication.models.FreecycleModel
import timber.log.Timber
import java.time.LocalDate

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    // TODO image intent launcher
    // TODO map intent launcher
    lateinit var app: MainApp
    private val binding get() = _binding!!
    var listing = FreecycleModel()
    private lateinit var addViewModel: AddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = MainApp()
        Timber.i("ON CREATE ADD FRAGMENT")
            //application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("ON CREATE VIEW ADD FRAGMENT")

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addViewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        addViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
            status -> status?.let {render(status)}
        })
        setButtonListener(binding)
        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.listingError), Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            //      listing.userId = app.user?.userId ?: 0
            listing.name = binding.name.text.toString()
            listing.contactNumber = binding.phoneNumber.text.toString()
            listing.listingTitle = binding.listingTitle.text.toString()
            listing.listingDescription = binding.listingDescription.text.toString()
            listing.itemAvailable = binding.toggleButton.isChecked
            val dateSelected = LocalDate.of(
                binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth
            )
            listing.dateAvailable = dateSelected

            if (listing.listingTitle.isNotEmpty() && listing.listingDescription.isNotEmpty() && listing.name.isNotEmpty()) {
//                if (edit) {
//                    app.listingsStore.update(listing.copy())
//                } else {
//                    app.listingsStore.create(listing.copy())
//                    Timber.i("add Button Pressed: $listing")
//                }
//                setResult(AppCompatActivity.RESULT_OK, intent.putExtra("updated_listing", listing))
//                finish()
                addViewModel.addListing(
                    FreecycleModel(name = listing.name, contactNumber = listing.contactNumber, listingTitle = listing.listingTitle, listingDescription = listing.listingDescription,
                    itemAvailable = listing.itemAvailable, dateAvailable = listing.dateAvailable )
                )
                // TODO how to close fragment/go to list and clear info from add
                Timber.i("ADDING LISTING %s", listing)
            } else {
                Snackbar.make(it, R.string.all_fields_required, Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO override fun onResume() {

}