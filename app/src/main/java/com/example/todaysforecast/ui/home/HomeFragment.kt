package com.example.todaysforecast.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todaysforecast.BuildConfig
import com.example.todaysforecast.R
import com.example.todaysforecast.databinding.HomeFragmentBinding
import com.example.todaysforecast.db.DataManager
import com.example.todaysforecast.model.bookmarked.BookmarkedCities
import com.example.todaysforecast.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.schibstedspain.leku.*
import com.schibstedspain.leku.permissions.PermissionUtils.isLocationPermissionGranted
import com.schibstedspain.leku.permissions.PermissionUtils.requestLocationPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewBinding: HomeFragmentBinding

    @Inject
    lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = HomeFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.selectCity.setOnClickListener {
            if (isLocationPermissionGranted(viewBinding.selectCity.context)) {
                selectCity()
            } else {
                requestLocationPermission(requireActivity())
            }
        }
        dataManager.unitType.asLiveData().observe(viewLifecycleOwner) {
            viewBinding.units.isChecked = it.equals("metric", true)
        }
        viewBinding.units.setOnCheckedChangeListener { buttonView, isChecked ->
            lifecycleScope.launch {
                dataManager.storeData(unitType = if (isChecked) "metric" else "imperial")
            }
        }
        viewModel.getForecastDao().getAllCities().observe(viewLifecycleOwner, {
            viewBinding.myCities.layoutManager = LinearLayoutManager(context)
            viewBinding.myCities.setHasFixedSize(true)
            viewBinding.myCities.adapter = HomeCitiesAdapter(
                it.toTypedArray(),
                ::setOnClickListener
            )
        })
        viewBinding.help.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToHelpFragment()
            )
        }
    }

    private fun setOnClickListener(isDelete: Boolean, bookmarkedCities: BookmarkedCities) {
        if (isDelete)
            showDeleteDialog(requireContext(), bookmarkedCities)
        else
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCityFragment(
                    bookmarkedCities
                )
            )
    }

    private fun selectCity() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withGooglePlacesApiKey(BuildConfig.GMP_KEY)
            .withGeolocApiKey(BuildConfig.GMP_KEY)
            .withDefaultLocaleSearchZone()
            .withSatelliteViewHidden()
            .withGoogleTimeZoneEnabled()
            .withUnnamedRoadHidden().withMapStyle(R.raw.map_style)
            .build(viewBinding.selectCity.context)
        locationPickerIntent.putExtra(ENABLE_LOCATION_PERMISSION_REQUEST, true)
        locationPickerIntent.putExtra(SEARCH_ZONE, "en_IN")
        startForAddressResult.launch(locationPickerIntent)
    }

    private val startForAddressResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val latitude = data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0
                    val longitude = data?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0
                    val locationData = Utils.geoAddress(
                        viewBinding.root.context,
                        latitude,
                        longitude
                    )
                    val city = locationData?.city.toString()
                    lifecycleScope.launch {
                        val bookmarkedCities = BookmarkedCities(
                            name = city,
                            latitude = latitude,
                            longitude = longitude
                        )
                        viewModel.getForecastDao().insert(bookmarkedCities)
                    }
                }

                else -> {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showDeleteDialog(context: Context, bookmarkedCities: BookmarkedCities) {
        val dialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val bottomSheet = inflater.inflate(R.layout.bottom_sheet, null)
        dialog.setContentView(bottomSheet)
        val btnClose = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_button)
        val btnOk = bottomSheet.findViewById<AppCompatButton>(R.id.ok_button)
        val subTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.sub_title)

        subTitle.text = context.getString(R.string.message_delete_city, bookmarkedCities.name)
        dialog.dismissWithAnimation = true
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.getForecastDao().deleteCity(bookmarkedCities)
            }
            dialog.dismiss()

        }
        dialog.show()
    }

}