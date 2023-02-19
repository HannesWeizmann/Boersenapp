package com.example.boersenapp.api.details.dataclass

data class Results(
    val active: Boolean,
    val address: Address,
    val branding: Branding,
    val cik: String,
    val composite_figi: String,
    val currency_name: String,
    val description: String,
    val homepage_url: String,
    val list_date: String,
    val locale: String,
    val market: String,
    val market_cap: Long,
    val name: String,
    val phone_number: String,
    val primary_exchange: String,
    val round_lot: Int,
    val share_class_figi: String,
    val share_class_shares_outstanding: Long,
    val sic_code: String,
    val sic_description: String,
    val ticker: String,
    val ticker_root: String,
    val total_employees: Int,
    val type: String,
    val weighted_shares_outstanding: Long
)