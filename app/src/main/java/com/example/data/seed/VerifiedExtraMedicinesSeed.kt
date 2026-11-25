package com.example.data.seed

import com.example.data.model.LocalizedText
import com.example.data.model.Medicine
import com.example.data.model.MedicineMonograph

object VerifiedExtraMedicinesSeed {

    val extraMedicines: List<Medicine> = listOf(
        Medicine(
            id = "clotrimazole_cream",
            name = LocalizedText(
                en = "Clotrimazole 1% Cream (Topical)",
                hi = "क्लोट्रिमाज़ोल 1% क्रीम (बाहरी उपयोग)",
                bn = "ক্লোট্রিমাজল ১% ক্রিম (ত্বকে ব্যবহার্য)"
            ),
            brandName = "Candid Cream / Clocip / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Clotrimazole",
                hi = "क्लोट्रिमाज़ोल",
                bn = "ক্লোট্রিমাজল"
            ),
            saltComposition = "Clotrimazole IP 1% w/w Cream",
            categoryId = "skin_care",
            manufacturerId = "mfr_glenmark",
            manufacturerDetails = LocalizedText(
                en = "Glenmark / Cipla Ltd. / PMBJP Jan Aushadhi",
                hi = "ग्लेनमार्क / सिप्ला लि. / पीएमबीजेपी जन औषधि",
                bn = "গ্লেনমার্ক / সিপলা লি. / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Topical treatment of fungal skin infections such as ringworm (tinea corporis), athlete's foot, and jock itch.",
                    hi = "दाद (रिंगवर्म), एथलीट फुट और फंगल त्वचा संक्रमण के बाहरी उपचार के लिए।",
                    bn = "দাদ, অ্যাথলিটস ফুট এবং ছত্রাকজনিত ত্বকের সংক্রমণের বাহ্যিক চিকিৎসায় ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Broad-spectrum topical azole antifungal listed on NLEM 2022 that clears dermatophyte and Candida skin infections without steroid side effects.",
                    hi = "NLEM 2022 में शामिल बिना स्टेरॉयड वाली एंटीफंगल क्रीम जो फंगल संक्रमण को जड़ से कम करती है।",
                    bn = "স্টেরয়েডমুক্ত NLEM 2022 তালিকাভুক্ত অ্যান্টিফাঙ্গাল ক্রিম যা ত্বকের ছত্রাক সংক্রমণ সারাতে কার্যকর।"
                ),
                howItWorks = LocalizedText(
                    en = "Inhibits ergosterol synthesis in fungal cell membranes, increasing permeability and causing fungal cell death.",
                    hi = "फंगल कोशिका झिल्ली में एर्गोस्टेरॉल के निर्माण को रोककर फंगस को नष्ट करता है।",
                    bn = "ছত্রাকের কোষপ্রাচীরে আর্গোস্টেরল তৈরি বন্ধ করে ছত্রাক ধ্বংস করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Mild local stinging, temporary redness, skin peeling, or mild itching at application site.",
                    hi = "लगाने के स्थान पर हल्की जलन, लालिमा या हल्की खुजली।",
                    bn = "লাগানোর স্থানে হালকা জ্বালাপোড়া, লালচেভাব বা সামান্য চুলকানি।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Severe contact dermatitis, blistering, oozing rash, or generalized allergic reaction.",
                    hi = "गंभीर संपर्क एलर्जी, छाले पड़ना, रिसाव या गंभीर चकत्ते।",
                    bn = "তীব্র কনট্যাক্ট ডার্মাটাইটিস, ফোস্কা পড়া বা গুরুতর অ্যালার্জিক প্রতিক্রিয়া।"
                ),
                warnings = LocalizedText(
                    en = "For external use only; avoid contact with eyes or mouth. Avoid mixing with unprescribed steroid creams which worsen fungal infections.",
                    hi = "केवल बाहरी उपयोग हेतु; आंखों व मुंह से दूर रखें। बिना सलाह स्टेरॉयड क्रीम न मिलाएं।",
                    bn = "শুধুমাত্র বাহ্যিক ব্যবহারের জন্য; চোখ ও মুখে লাগাবেন না। স্টেরয়েড মেশানো ক্রিম এড়িয়ে চলুন।"
                ),
                drugInteractions = LocalizedText(
                    en = "Minimal systemic absorption; latex condoms/diaphragms may be weakened if applied to genital skin.",
                    hi = "रक्त में अवशोषण न्यूनतम; जननांग क्षेत्र में उपयोग पर लेटेक्स उत्पाद कमजोर हो सकते हैं।",
                    bn = "রক্তে শোষণ নগণ্য; ল্যাটেক্স কন্ডোমের কার্যকারিতা কমে যেতে পারে।"
                ),
                contraindications = LocalizedText(
                    en = "Known hypersensitivity to clotrimazole or any cream base excipient.",
                    hi = "क्लोट्रिमाज़ोल या क्रीम के किसी घटक से ज्ञात एलर्जी।",
                    bn = "ক্লোট্রিমাজল বা ক্রিমের উপাদানে অ্যালার্জি থাকলে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Topical skin use has very low systemic absorption; consult a doctor before extensive or internal/vaginal use during pregnancy.",
                    hi = "त्वचा पर बाहरी उपयोग में अवशोषण कम होता है, फिर भी गर्भावस्था में डॉक्टर से सलाह लें।",
                    bn = "বাহ্যিক ব্যবহারে রক্তে শোষণ কম হলেও গর্ভাবস্থায় চিকিৎসকের পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Keep tube tightly capped below 25°C; do not freeze.",
                    hi = "ट्यूब का ढक्कन कसकर बंद कर 25°C से कम तापमान पर रखें; जमाएं नहीं।",
                    bn = "টিউবের মুখ ভালোভাবে বন্ধ করে ২৫°সে-এর নিচে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO India, NLEM 2022, U.S. FDA OpenFDA Clotrimazole Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "clotrimazole",
            popularScore = 790L,
            published = true
        ),
        Medicine(
            id = "ambroxol_syrup",
            name = LocalizedText(
                en = "Ambroxol 30 mg/5 mL Oral Syrup",
                hi = "एम्ब्रोक्सोल 30 mg/5 mL सिरप",
                bn = "অ্যামব্রক্সল ৩০ মিগ্রা/৫ মিলি সিরাপ"
            ),
            brandName = "Mucolite / Ambrodil / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Ambroxol Hydrochloride",
                hi = "एम्ब्रोक्सोल हाइड्रोक्लोराइड",
                bn = "অ্যামব্রক্সল হাইড্রোক্লোরাইড"
            ),
            saltComposition = "Ambroxol Hydrochloride IP 30 mg per 5 mL",
            categoryId = "cough_cold",
            manufacturerId = "mfr_drreddys",
            manufacturerDetails = LocalizedText(
                en = "Dr. Reddy's / Aristo / PMBJP Jan Aushadhi",
                hi = "डॉ. रेड्डीज / अरिस्टो / पीएमबीजेपी जन औषधि",
                bn = "ড. রেড্ডিজ / অ্যারিস্টো / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Symptomatic relief of productive (wet/chesty) cough associated with thick bronchial mucus.",
                    hi = "गाढ़े बलगम वाली गीली खांसी में कफ को पतला कर बाहर निकालने के लिए।",
                    bn = "ঘন কফযুক্ত ভেজা কাশিতে কফ পাতলা করে বের করতে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Mucolytic agent that reduces mucus viscosity and aids mucociliary clearance in acute or chronic respiratory bronchitis.",
                    hi = "म्यूकोलिटिक दवा जो श्वसन नलियों से गाढ़े कफ को ढीला कर सांस लेना आसान बनाती है।",
                    bn = "মিউকোলাইটিক ওষুধ যা শ্বাসনালির ঘন কফ তরল করে শ্বাসপ্রশ্বাস সহজ করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Stimulates serous gland secretion, cleaves acid mucopolysaccharide fibers in sputum, and stimulates pulmonary surfactant synthesis.",
                    hi = "बलगम के गाढ़े रेशों को तोड़कर उसे पतला करता है और फेफड़ों में सरफेक्टेंट स्राव बढ़ाता है।",
                    bn = "কফের আঠালো তন্তু ভেঙে তরল করে ও ফুসফুসের সারফ্যাক্ট্যান্ট নিঃসরণ বাড়ায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Mild nausea, altered taste, mild throat/oral numbness, mild stomach discomfort.",
                    hi = "हल्की मतली, स्वाद में बदलाव, मुंह या गले में हल्का सुन्नपन, पेट में हल्की असहजता।",
                    bn = "হালকা বমি ভাব, স্বাদের পরিবর্তন, পেটে সামান্য অস্বস্তি।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Rare severe cutaneous adverse reactions (Stevens-Johnson syndrome, toxic epidermal necrolysis), anaphylaxis, angioedema.",
                    hi = "दुर्लभ गंभीर त्वचा प्रतिक्रिया (SJS/TEN), तीव्र एलर्जी, चेहरे/गले में सूजन।",
                    bn = "বিরল গুরুতর ত্বকের প্রতিক্রিয়া (SJS/TEN), তীব্র অ্যালার্জি বা অ্যানজিওইডিমা।"
                ),
                warnings = LocalizedText(
                    en = "Seek prompt care if progressive skin rash or mucosal lesions occur, or if cough persists beyond 7 days with fever. Use caution in peptic ulcer history.",
                    hi = "त्वचा पर चकत्ते होने या बुखार के साथ 7 दिन से अधिक खांसी रहने पर तुरंत डॉक्टर से मिलें। अल्सर रोगी सावधानी बरतें।",
                    bn = "ত্বকে র‍্যাশ দেখা দিলে বা ৭ দিনের বেশি জ্বর-কাশি থাকলে দ্রুত চিকিৎসক দেখান।"
                ),
                drugInteractions = LocalizedText(
                    en = "Avoid simultaneous antitussive (cough-suppressant) syrups which can trap loosened mucus in airways.",
                    hi = "सूखी खांसी रोकने वाले कफ सप्रेसेंट सिरप साथ न लें, इससे ढीला हुआ बलगम फेफड़ों में फंस सकता है।",
                    bn = "শুষ্ক কাশি দমনকারী ওষুধের সাথে একত্রে সেবন এড়িয়ে চলুন।"
                ),
                contraindications = LocalizedText(
                    en = "Known hypersensitivity to ambroxol or bromhexine; hereditary fructose intolerance (for sorbitol-containing syrups).",
                    hi = "एम्ब्रोक्सोल या ब्रोमहेक्सिन से एलर्जी।",
                    bn = "অ্যামব্রক্সল বা ব্রোমহেক্সিনে অ্যালার্জি থাকলে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Avoid during first trimester of pregnancy unless clearly advised by a physician; consult doctor while breastfeeding.",
                    hi = "गर्भावस्था की पहली तिमाही में बिना चिकित्सकीय सलाह के न लें; स्तनपान में डॉक्टर से पूछें।",
                    bn = "গর্ভাবস্থার প্রথম তিন মাসে চিকিৎসকের পরামর্শ ছাড়া ব্যবহার অনুচিত।"
                ),
                storageInstructions = LocalizedText(
                    en = "Keep bottle tightly closed below 25–30°C away from direct sunlight; use calibrated measuring cup.",
                    hi = "बोतल कसकर बंद कर 25–30°C से कम तापमान पर रखें; मापक कप का उपयोग करें।",
                    bn = "বোতল ভালোভাবে বন্ধ করে ঠান্ডা ও অন্ধকার স্থানে রাখুন; পরিমাপক কাপ ব্যবহার করুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO Approved Oral Monograph, EMA PRAC Safety Summary",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "guaifenesin",
            popularScore = 760L,
            published = true
        ),
        Medicine(
            id = "carboxymethylcellulose_drops",
            name = LocalizedText(
                en = "Carboxymethylcellulose 0.5% Eye Drops",
                hi = "कार्बोक्सीमिथाइलसेलुलोज़ 0.5% आई ड्रॉप्स",
                bn = "কার্বক্সিমিথাইলসেলুলোজ ০.৫% চোখের ড্রপ"
            ),
            brandName = "Refresh Tears / Lubrex / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Carboxymethylcellulose Sodium",
                hi = "कार्बोक्सीमिथाइलसेलुलोज़ सोडियम",
                bn = "কার্বক্সিমিথাইলসেলুলোজ সোডিয়াম"
            ),
            saltComposition = "Carboxymethylcellulose Sodium IP 0.5% w/v",
            categoryId = "eye_ear",
            manufacturerId = "mfr_cipla",
            manufacturerDetails = LocalizedText(
                en = "Allergan / Cipla Ltd. / PMBJP Jan Aushadhi",
                hi = "एलर्गन / सिप्ला लि. / पीएमबीजेपी जन औषधि",
                bn = "অ্যালারগান / সিপলা লি. / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Temporary relief of burning, dryness, irritation, and discomfort due to dry eyes or wind/screen exposure.",
                    hi = "आंखों के सूखेपन, जलन, खुजली और स्क्रीन या धूप से होने वाली असहजता से राहत के लिए।",
                    bn = "চোখের শুষ্কতা, জ্বালাপোড়া এবং কম্পিউটার বা বাতাসের কারণে হওয়া অস্বস্তি উপশমে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Artificial tear ocular lubricant on India NLEM 2022 that stabilizes the tear film and protects the corneal surface.",
                    hi = "NLEM 2022 में शामिल कृत्रिम आंसू जो आंखों की नमी बनाए रख कॉर्निया की सुरक्षा करता है।",
                    bn = "NLEM 2022 তালিকাভুক্ত কৃত্রিম অশ্রু যা চোখের আর্দ্রতা বজায় রাখে।"
                ),
                howItWorks = LocalizedText(
                    en = "Binds to corneal epithelial cells via mucoadhesive polymers, prolonging tear retention time and reducing ocular friction.",
                    hi = "आंख की सतह पर सुरक्षात्मक नमी की परत बनाकर घर्षण और सूखापन कम करता है।",
                    bn = "চোখের পৃষ্ঠে আর্দ্রতার একটি সুরক্ষাস্তর তৈরি করে ঘর্ষণ ও শুষ্কতা কমায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Brief mild blurring of vision immediately after instillation, slight transient stinging.",
                    hi = "बूंद डालने के तुरंत बाद कुछ सेकंड के लिए धुंधला दिखना, हल्की क्षणिक चुभन।",
                    bn = "ড্রপ দেওয়ার পর কয়েক সেকেন্ড ঝাপসা দেখা বা হালকা খসখসে ভাব।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Persistent eye pain, vision changes, worsening redness, or pus-like discharge (stop and seek urgent eye exam).",
                    hi = "लगातार आंख में दर्द, दृष्टि में बदलाव, बढ़ती लालिमा या मवाद जैसा स्राव (तुरंत नेत्र चिकित्सक से मिलें)।",
                    bn = "ক্রমাগত চোখে ব্যথা, দৃষ্টিশক্তির পরিবর্তন বা তীব্র লালভাব হলে দ্রুত চক্ষু চিকিৎসকের পরামর্শ নিন।"
                ),
                warnings = LocalizedText(
                    en = "Do not touch the dropper tip to any surface to avoid contamination. Discard multidose bottle 30 days after opening; remove contact lenses before instilling preserved drops.",
                    hi = "ड्रॉपर की नोक को आंख या किसी सतह से न छुएं। खुलने के 30 दिन बाद बोतल न इस्तेमाल करें।",
                    bn = "ড্রপারের মুখ কোনো কিছুর সাথে স্পর্শ করবেন না। খোলার ৩০ দিন পর বোতল ফেলে দিন।"
                ),
                drugInteractions = LocalizedText(
                    en = "If using other ophthalmic drops, wait at least 5–10 minutes between instillations so the first drop is not washed out.",
                    hi = "अन्य आई ड्रॉप के साथ उपयोग करने पर कम से कम 5–10 मिनट का अंतर रखें।",
                    bn = "অন্য চোখের ড্রপ ব্যবহার করলে অন্তত ৫–১০ মিনিট বিরতি দিন।"
                ),
                contraindications = LocalizedText(
                    en = "Hypersensitivity to carboxymethylcellulose or preservative ingredients in multidose formulations.",
                    hi = "कार्बोक्सीमिथाइलसेलुलोज़ या प्रिजर्वेटिव घटक से एलर्जी।",
                    bn = "কার্বক্সিমিথাইলসেলুলোজ বা প্রিজারভেটিভে অ্যালার্জি থাকলে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Minimal systemic absorption from ocular drops; consult an ophthalmologist before prolonged use during pregnancy or breastfeeding.",
                    hi = "आंखों से रक्त में अवशोषण न्यूनतम है; लंबे उपयोग से पहले डॉक्टर से पूछें।",
                    bn = "চোখের ড্রপ থেকে রক্তে শোষণ নগণ্য; দীর্ঘ ব্যবহারে চিকিৎসকের পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 25°C in a clean dark place; do not freeze.",
                    hi = "25°C से कम तापमान पर साफ जगह में रखें; जमाएं नहीं।",
                    bn = "২৫°সে-এর নিচে পরিষ্কার ও ঠান্ডা স্থানে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO Ophthalmic Standard, NLEM 2022, U.S. FDA OTC Ophthalmic Monograph",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "carboxymethylcellulose",
            popularScore = 735L,
            published = true
        ),
        Medicine(
            id = "ferrous_folate",
            name = LocalizedText(
                en = "Ferrous Sulfate & Folic Acid Tablet",
                hi = "फेरस सल्फेट और फोलिक एसिड टैबलेट",
                bn = "ফেরাস সালফেট ও ফলিক অ্যাসিড ট্যাবলেট"
            ),
            brandName = "Fefol / Autrin / Jan Aushadhi IFA",
            genericName = LocalizedText(
                en = "Ferrous Sulfate + Folic Acid (IFA)",
                hi = "फेरस सल्फेट + फोलिक एसिड",
                bn = "ফেরাস সালফেট + ফলিক অ্যাসিড"
            ),
            saltComposition = "Elemental Iron 100 mg + Folic Acid IP 500 mcg",
            categoryId = "womens_health",
            manufacturerId = "mfr_gsk",
            manufacturerDetails = LocalizedText(
                en = "GSK / Abbott India / PMBJP Jan Aushadhi (Anemia Mukt Bharat)",
                hi = "जीएसके / एबॉट इंडिया / पीएमबीजेपी जन औषधि",
                bn = "জিএসকে / অ্যাবট ইন্ডিয়া / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Prevention and treatment of iron-deficiency anemia, megaloblastic anemia, and nutritional anemia during pregnancy and menstruation.",
                    hi = "आयरन की कमी से होने वाले एनीमिया और गर्भावस्था व मासिक धर्म में पोषण संबंधी रक्तअल्पता की रोकथाम व उपचार के लिए।",
                    bn = "আয়রনের ঘাটতিজনিত রক্তাল্পতা (অ্যানিমিয়া) প্রতিরোধ ও গর্ভাবস্থায় হিমোগ্লোবিন বাড়াতে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Essential NLEM 2022 hematinic combination supporting hemoglobin synthesis, oxygen transport, and neural tube health in pregnancy.",
                    hi = "NLEM 2022 में शामिल आवश्यक हिमैटिनिक संयोजन जो हीमोग्लोबिन निर्माण और गर्भस्थ शिशु के विकास में सहायक है।",
                    bn = "NLEM 2022 তালিকাভুক্ত আয়রন-ফলিক অ্যাসিড যা হিমোগ্লোবিন তৈরি ও গর্ভস্থ শিশুর সুস্থ বিকাশে অপরিহার্য।"
                ),
                howItWorks = LocalizedText(
                    en = "Supplies elemental iron required for hemoglobin and red blood cell production while folic acid supports DNA synthesis and erythropoiesis.",
                    hi = "हीमोग्लोबिन और लाल रक्त कणिकाओं के लिए आयरन तथा DNA व रक्त निर्माण के लिए फोलिक एसिड प्रदान करता है।",
                    bn = "লোহিত রক্তকণিকা ও হিমোগ্লোবিন গঠনে আয়রন এবং ডিএনএ সংশ্লেষণে ফলিক অ্যাসিড সরবরাহ করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Dark/black stools (harmless iron effect), mild nausea, constipation, abdominal cramping, metallic taste.",
                    hi = "मल का रंग गहरा/काला होना (सामान्य), हल्की मतली, कब्ज, पेट में हल्की मरोड़।",
                    bn = "মলের রঙ কালো হওয়া (স্বাভাবিক), হালকা বমি ভাব, কোষ্ঠকাঠিন্য বা পেটে অস্বস্তি।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Accidental overdose in children causes fatal acute iron toxicity (severe vomiting, bloody diarrhea, lethargy, liver shock — emergency!).",
                    hi = "बच्चों में गलती से अधिक सेवन से घातक आयरन विषाक्तता (गंभीर उल्टी, खूनी दस्त, बेहोशी — तुरंत आपातकालीन इलाज कराएं)।",
                    bn = "শিশুদের দুর্ঘটনাবশত অতিরিক্ত সেবনে মারাত্মক আয়রন বিষক্রিয়া ঘটে — দ্রুত জরুরি চিকিৎসা প্রয়োজন।"
                ),
                warnings = LocalizedText(
                    en = "Keep strictly out of reach of children. Separate from tea, coffee, milk, or calcium tablets by 2 hours to maximize iron absorption.",
                    hi = "बच्चों की पहुंच से सख्ती से दूर रखें। बेहतर अवशोषण के लिए चाय, कॉफी, दूध या कैल्शियम से 2 घंटे का अंतर रखें।",
                    bn = "শিশুদের নাগালের বাইরে রাখুন। ভালো শোষণের জন্য চা, কফি, দুধ বা ক্যালসিয়াম থেকে ২ ঘণ্টা ব্যবধানে খান।"
                ),
                drugInteractions = LocalizedText(
                    en = "Calcium supplements, antacids, levothyroxine, tetracyclines, fluoroquinolones (ciprofloxacin), and methyldopa (separate by 2–4 hours).",
                    hi = "कैल्शियम, एंटासिड, लेवोथायरोक्सिन, टेट्रासाइक्लिन और सिप्रोफ्लोक्सासिन से 2–4 घंटे का अंतर रखें।",
                    bn = "ক্যালসিয়াম, অ্যান্টাসিড, লেভোথাইরক্সিন, সিপ্রোফ্লক্সাসিন থেকে ২–৪ ঘণ্টা ব্যবধানে সেবনীয়।"
                ),
                contraindications = LocalizedText(
                    en = "Hemochromatosis, hemosiderosis, hemolytic anemias without iron deficiency, or repeated blood transfusions.",
                    hi = "हीमोक्रोमैटोसिस (शरीर में अधिक आयरन जमाव), हीमोलिटिक एनीमिया या बार-बार रक्त आधान।",
                    bn = "হিমোক্রোমাটোসিস বা আয়রনের অভাববিহীন রক্তাল্পতায় নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Widely recommended under antenatal care guidelines when prescribed by a doctor to prevent maternal anemia and neural tube defects.",
                    hi = "प्रसवपूर्व देखभाल में डॉक्टर द्वारा निर्धारित किए जाने पर गर्भावस्था और स्तनपान में नियमित रूप से अनुशंसित।",
                    bn = "গর্ভাবস্থায় চিকিৎসকের নির্দেশিত মাত্রায় মাতৃস্বাস্থ্য ও শিশুর সুরক্ষায় নিয়মিত নির্দেশিত।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in dry child-resistant packaging away from moisture.",
                    hi = "30°C से कम तापमान पर सूखे सुरक्षित पैक में बच्चों से दूर रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক ও শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO, India NLEM 2022, MoHFW Anemia Mukt Bharat Monograph",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "ferrous sulfate",
            popularScore = 810L,
            published = true
        ),
        Medicine(
            id = "ors_who_sachet",
            name = LocalizedText(
                en = "Oral Rehydration Salts (WHO Low-Osmolarity ORS)",
                hi = "ओआरएस (WHO लो-ऑस्मोलैरिटी जीवन रक्षक घोल)",
                bn = "ওআরএস (WHO লো-অসমোলারিটি স্যালাইন)"
            ),
            brandName = "Electral / Walyte / Jan Aushadhi ORS",
            genericName = LocalizedText(
                en = "Oral Rehydration Salts (WHO Formula)",
                hi = "ओरल रिहाइड्रेशन सॉल्ट्स (WHO सूत्र)",
                bn = "ওরাল রিহাইড্রেশন সল্টস (WHO ফর্মুলা)"
            ),
            saltComposition = "Sodium Chloride 2.6g + Potassium Chloride 1.5g + Sodium Citrate 2.9g + Dextrose 13.5g (per L)",
            categoryId = "childrens_medicines",
            manufacturerId = "mfr_bppi",
            manufacturerDetails = LocalizedText(
                en = "FDC Ltd. / Wallance / PMBJP Jan Aushadhi",
                hi = "एफडीसी लि. / वालेस / पीएमबीजेपी जन औषधि",
                bn = "এফডিসি লি. / ওয়্যালেস / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "First-line prevention and treatment of dehydration and electrolyte loss due to acute diarrhea or vomiting in children and adults.",
                    hi = "बच्चों और वयस्कों में दस्त या उल्टी से होने वाले निर्जलीकरण और इलेक्ट्रोलाइट की कमी के उपचार के लिए।",
                    bn = "শিশু ও বড়দের ডায়রিয়া বা বমিজনিত পানিশূন্যতা ও লবণ-জল ঘাটতি পূরণে প্রথম সারির চিকিৎসা।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "WHO/NLEM 2022 low-osmolarity standard reduces stool output, vomiting, and need for IV fluids safely when mixed correctly.",
                    hi = "WHO व NLEM 2022 मानक लो-ऑस्मोलैरिटी घोल सही मात्रा पानी में मिलाने पर निर्जलीकरण को सुरक्षित रोकता है।",
                    bn = "WHO ও NLEM 2022 অনুমোদিত লো-অসমোলারিটি ওআরএস সঠিকভাবে গুলিয়ে পান করলে ডিহাইড্রেশন প্রতিরোধ করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Sodium-glucose cotransport across intact intestinal mucosa draws water and essential electrolytes rapidly into the bloodstream.",
                    hi = "आंतों की परत में सोडियम-ग्लूकोज सह-परिवहन द्वारा पानी और आवश्यक लवणों को तेजी से शरीर में पहुंचाता है।",
                    bn = "অন্ত্রে সোডিয়াম-গ্লুকোজ কো-ট্রান্সপোর্টের মাধ্যমে দ্রুত জল ও প্রয়োজনীয় ইলেক্ট্রোলাইট শোষণ নিশ্চিত করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Generally none when prepared correctly; mild vomiting if sipped too rapidly (give small frequent sips/spoonfuls).",
                    hi = "सही से घोलने पर आमतौर पर कोई दुष्प्रभाव नहीं; बहुत तेजी से पीने पर हल्की उल्टी हो सकती है।",
                    bn = "সঠিক নিয়মে গুলিয়ে ধীরে ধীরে পান করলে সাধারণত কোনো পার্শ্বপ্রতিক্রিয়া নেই।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Hypernatremia or fluid overload if mixed with less water than directed; seek urgent pediatric care if child is lethargic, sunken-eyed, unable to drink, or has bloody stools.",
                    hi = "कम पानी में घोलने से सोडियम अधिकता; बच्चा सुस्त हो, पानी न पी पाए या मल में खून आए तो तुरंत अस्पताल ले जाएं।",
                    bn = "কম জলে গুললে অতিরিক্ত সোডিয়ামের ঝুঁকি; শিশু নিস্তেজ হলে, জল পান করতে না পারলে বা রক্ত পায়খানা হলে জরুরি চিকিৎসা নিন।"
                ),
                warnings = LocalizedText(
                    en = "Dissolve the full 21.8 g sachet strictly in 1 liter (or 4.3 g mini-sachet in 200 mL) of clean boiled-and-cooled drinking water. Discard unused solution after 24 hours; never boil ORS after mixing.",
                    hi = "पूरा 21.8 ग्राम पैकेट ठीक 1 लीटर साफ ठंडे उबले पानी में घोलें। 24 घंटे बाद बचा घोल फेंक दें; घोल को उबालें नहीं।",
                    bn = "পুরো ২১.৮ গ্রাম প্যাকেট ঠিক ১ লিটার বিশুদ্ধ ঠান্ডা ফোটানো জলে গুলুন। ২৪ ঘণ্টা পর অবশিষ্ট দ্রবণ ফেলে দিন; গরম করবেন না।"
                ),
                drugInteractions = LocalizedText(
                    en = "No clinically significant drug interactions at standard hydration use; zinc supplementation may be co-prescribed in pediatric diarrhea per guidelines.",
                    hi = "सामान्य उपयोग में कोई महत्वपूर्ण दवा परस्पर क्रिया नहीं; बच्चों में डॉक्टर की सलाह से जिंक सिरप दिया जा सकता है।",
                    bn = "স্বাভাবিক ব্যবহারে কোনো গুরুত্বপূর্ণ ওষুধের মিথস্ক্রিয়া নেই।"
                ),
                contraindications = LocalizedText(
                    en = "Severe shock requiring immediate IV resuscitation, persistent intractable vomiting, paralytic ileus, intestinal obstruction, or severe kidney failure with anuria.",
                    hi = "गंभीर शॉक (IV ड्रिप आवश्यक), लगातार न रुकने वाली उल्टी या आंतों में रुकावट।",
                    bn = "তীব্র শক, অবিরাম বমি, অন্ত্রের প্রতিবন্ধকতা বা প্রস্রাব বন্ধ থাকা কিডনি বিকল অবস্থায় মুখে নয়।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Safe during pregnancy and breastfeeding for oral rehydration; consult a physician if diarrhea is severe or persistent.",
                    hi = "गर्भावस्था व स्तनपान में सुरक्षित; गंभीर या लगातार दस्त होने पर डॉक्टर को दिखाएं।",
                    bn = "গর্ভাবস্থা ও স্তন্যদানে নিরাপদ; তীব্র ডায়রিয়ায় চিকিৎসকের পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store sealed sachets in a cool dry place below 30°C; protect from clumping moisture.",
                    hi = "बंद पैकेट को 30°C से कम तापमान पर सूखी जगह रखें ताकि पाउडर जमे नहीं।",
                    bn = "মুখবন্ধ প্যাকেট ৩০°সে-এর নিচে শুষ্ক ও ঠান্ডা স্থানে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "WHO Low-Osmolarity ORS Standard, CDSCO, India NLEM 2022",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "",
            popularScore = 845L,
            published = true
        )
    )
}
