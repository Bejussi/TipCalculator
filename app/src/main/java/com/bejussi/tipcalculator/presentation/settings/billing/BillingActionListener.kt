package com.bejussi.tipcalculator.presentation.settings.billing

import com.android.billingclient.api.ProductDetails

interface BillingActionListener {

    fun startBilling(productDetails: ProductDetails)
}