# FinTrack - Limitations & Future Roadmap

## 1. Current Architecture Highlights
FinTrack v1.0 delivers an **offline-first**, production-quality personal finance application with:
- Arbitrary precision decimal calculations (`BigDecimal`).
- Accurate double-entry ledger without income/expense inflation on transfers.
- Net Worth calculation (`Total Assets - Total Liabilities`).
- Immutable financial audit trail tracking creation, edits, deletions, and balance adjustments.
- Intelligent rule-based spending insights engine.
- Complete data ownership via encrypted JSON database backup, CSV exports, and printable HTML/PDF monthly statements.
- Nepal-specific localization (NPR `रु`, Nepali string resources, Bikram Sambat date conversion, local payment methods e.g., eSewa, Khalti, Fonepay).

---

## 2. Current Known Limitations
1. **Device-Local Storage Only**:
   - In alignment with the strict offline privacy mandate, data is stored locally in SQLite (`fintrack.db`). Multi-device live sync currently requires restoring a exported JSON backup file on the target device.
2. **Static Bank Statement Import**:
   - The application does not automatically screen-scrape or establish open-banking API connections with commercial banks due to banking regulatory limitations in Nepal and offline design requirements. Bank reconciliation is handled manually via the Account Reconciliation module.
3. **Bikram Sambat Almanac Extents**:
   - The embedded Bikram Sambat date converter covers modern contemporary operational years (2080-2083+ BS) based on reference offsets. Extended historical conversions prior to 2050 BS require continuous astrological astronomical leap-day tables.

---

## 3. Future Roadmap & Enhancement Suggestions

### Phase A: End-to-End Encrypted Cloud Synchronization
- Introduce optional client-side encrypted sync (e.g., using Google Drive AppData folder or a self-hosted Nextcloud / WebDAV / Supabase backend).
- Ensure the decryption key resides exclusively on the user's device (zero-knowledge encryption).

### Phase B: On-Device Machine Learning & OCR Receipt Scanning
- Integrate Google ML Kit on-device Text Recognition (Vision API) to automatically extract:
  - Merchant name
  - Total transaction amount
  - Date and time from photo receipts.

### Phase C: Shared Family / Household Ledgers
- Add multi-profile support allowing couples or flatmates to track shared rent, grocery bills, and split expenses using proportional splitting algorithms (e.g. 50/50, custom ratios).

### Phase D: Bank SMS Auto-Parsing
- Add optional on-device SMS parsing permissions to automatically detect incoming debit and credit transaction SMS from major Nepali and international banks (Nabil, NIC Asia, Global IME, eSewa, Khalti OTP notifications) and pre-fill quick expense drafts.

