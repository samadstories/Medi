package com.example.data.seed

import com.example.data.model.LocalizedText
import com.example.data.model.Medicine
import com.example.data.model.MedicineMonograph

object VerifiedMedicinesSeed {

    val coreMedicines: List<Medicine> = listOf(
        Medicine(
            id = "paracetamol_500",
            name = LocalizedText(
                en = "Paracetamol 500 mg Tablet",
                hi = "पैरासिटामोल 500 mg टैबलेट",
                bn = "প্যারাসিটামল ৫০০ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Dolo 500 / Calpol 500 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Paracetamol (Acetaminophen)",
                hi = "पैरासिटामोल (एसिटामिनोफेन)",
                bn = "প্যারাসিটামল (অ্যাসিটামিনোফেন)"
            ),
            saltComposition = "Paracetamol IP 500 mg",
            categoryId = "fever_pain",
            manufacturerId = "mfr_micro",
            manufacturerDetails = LocalizedText(
                en = "Micro Labs Ltd. (Bengaluru) / GSK / PMBJP Jan Aushadhi GMP Units",
                hi = "माइक्रो लैब्स लि. (बेंगलुरु) / जीएसके / पीएमबीजेपी जन औषधि जीएमपी इकाइयां",
                bn = "মাইক্রো ল্যাবস লিমিটেড (বেঙ্গালুরু) / জিএসকে / পিএমবিজেপি জনঔষধি জিএমপি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Relief of mild-to-moderate pain (headache, toothache, body ache) and reduction of fever.",
                    hi = "हल्के से मध्यम दर्द (सिरदर्द, दांत दर्द, शरीर दर्द) और बुखार को कम करने के लिए।",
                    bn = "হালকা থেকে মাঝারি ব্যথা (মাথাব্যথা, দাঁতব্যথা, গা-হাত-পা ব্যথা) ও জ্বর কমাতে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Effective antipyretic and analgesic included in WHO & India NLEM 2022 for safe fever and pain management when taken within prescribed limits.",
                    hi = "निर्धारित खुराक में बुखार और दर्द प्रबंधन के लिए WHO और भारत की NLEM 2022 में शामिल प्रभावी ज्वरनाशक और दर्द निवारक।",
                    bn = "নির্ধারিত মাত্রায় জ্বর ও ব্যথা উপশমে WHO এবং ভারতের NLEM 2022 তালিকাভুক্ত কার্যকর অ্যান্টিপাইরেটিক ও অ্যানালজেসিক।"
                ),
                howItWorks = LocalizedText(
                    en = "Inhibits prostaglandin synthesis primarily in the central nervous system to block pain signals and regulate the hypothalamic heat-regulating center.",
                    hi = "केंद्रीय तंत्रिका तंत्र में प्रोस्टाग्लैंडीन के निर्माण को रोककर दर्द संकेतों को कम करता है और मस्तिष्क के तापमान केंद्र को नियंत्रित करता है।",
                    bn = "সেন্ট্রাল নার্ভাস সিস্টেমে প্রোস্টাগ্ল্যান্ডিন সংশ্লেষণ কমিয়ে ব্যথার সংকেত ও মস্তিষ্কের তাপ-নিয়ন্ত্রণ কেন্দ্র নিয়ন্ত্রণ করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Generally well tolerated at recommended doses; occasional mild nausea or stomach discomfort.",
                    hi = "अनुशंसित खुराक पर आमतौर पर सुरक्षित; कभी-कभी हल्की मतली या पेट में असहजता।",
                    bn = "নির্ধারিত মাত্রায় সাধারণত সহনীয়; মাঝে মাঝে হালকা বমি ভাব বা পেটে অস্বস্তি হতে পারে।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Severe hepatotoxicity (liver injury) on overdose, rare allergic skin reactions (Stevens-Johnson syndrome), swelling of face or breathing difficulty.",
                    hi = "अधिक खुराक से गंभीर लिवर क्षति (हेपेटोटॉक्सिसिटी), दुर्लभ गंभीर त्वचा एलर्जी (SJS), चेहरे पर सूजन या सांस लेने में कठिनाई।",
                    bn = "অতিরিক্ত মাত্রায় গুরুতর লিভারের ক্ষতি, বিরল ত্বকের তীব্র অ্যালার্জি (SJS), মুখ ফুলে যাওয়া বা শ্বাসকষ্ট।"
                ),
                warnings = LocalizedText(
                    en = "Do not exceed total daily adult limit without medical advice. Check other cold/pain products for hidden paracetamol to avoid accidental double dosing.",
                    hi = "चिकित्सीय सलाह के बिना दैनिक अधिकतम सीमा से अधिक न लें। अन्य दवाओं में छिपे पैरासिटामोल की जांच अवश्य करें।",
                    bn = "চিকিৎসকের পরামর্শ ছাড়া দৈনিক সর্বোচ্চ মাত্রা অতিক্রম করবেন না। অন্যান্য সর্দি-ব্যথার ওষুধে প্যারাসিটামল আছে কিনা দেখে নিন।"
                ),
                drugInteractions = LocalizedText(
                    en = "Chronic alcohol use, warfarin (increased bleeding risk with prolonged high doses), isoniazid, carbamazepine, and phenytoin.",
                    hi = "नियमित शराब सेवन, वारफेरिन (रक्तस्राव जोखिम), आइसोनियाजिड, कार्बामाज़ेपाइन और फेनिटोइन के साथ सावधानी।",
                    bn = "নিয়মিত অ্যালকোহল, ওয়ারফারিন (রক্তপাতের ঝুঁকি), আইসোনিয়াজিড, কার্বামাজেপিন ও ফেনাইটয়েনের সাথে সতর্কতা।"
                ),
                contraindications = LocalizedText(
                    en = "Severe hepatic impairment, acute liver failure, or documented hypersensitivity to paracetamol.",
                    hi = "गंभीर लिवर रोग, तीव्र लिवर विफलता या पैरासिटामोल से ज्ञात एलर्जी।",
                    bn = "গুরুতর লিভারের রোগ, অ্যাকিউট লিভার ফেইলিউর বা প্যারাসিটামলে অতিসংবেদনশীলতা।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Generally considered compatible during pregnancy and breastfeeding when used at the lowest effective dose for the shortest duration under a doctor's guidance.",
                    hi = "डॉक्टर की सलाह से न्यूनतम प्रभावी खुराक और सबसे कम अवधि के लिए गर्भावस्था व स्तनपान में आमतौर पर सुरक्षित माना जाता है।",
                    bn = "চিকিৎসকের পরামর্শে সর্বনিম্ন কার্যকর মাত্রায় ও স্বল্প সময়ের জন্য গর্ভাবস্থা ও স্তন্যদানে সাধারণত নিরাপদ ধরা হয়।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in a dry place protected from moisture and direct sunlight. Keep out of reach of children.",
                    hi = "30°C से कम तापमान पर सूखी जगह में नमी व धूप से दूर रखें। बच्चों की पहुंच से दूर रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক ও আলো-আর্দ্রতামুক্ত স্থানে এবং শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO India, NLEM 2022, U.S. FDA OpenFDA Acetaminophen Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "acetaminophen",
            popularScore = 980L,
            published = true
        ),
        Medicine(
            id = "azithromycin_500",
            name = LocalizedText(
                en = "Azithromycin 500 mg Tablet",
                hi = "एज़िथ्रोमाइसिन 500 mg टैबलेट",
                bn = "অ্যাজিথ্রোমাইসিন ৫০০ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Azithral 500 / Azee 500 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Azithromycin",
                hi = "एज़िथ्रोमाइसिन",
                bn = "অ্যাজিথ্রোমাইসিন"
            ),
            saltComposition = "Azithromycin IP 500 mg",
            categoryId = "antibiotics",
            manufacturerId = "mfr_cipla",
            manufacturerDetails = LocalizedText(
                en = "Alembic / Cipla Ltd. / PMBJP Jan Aushadhi Approved Manufacturers",
                hi = "एलेम्बिक / सिप्ला लि. / पीएमबीजेपी जन औषधि अनुमोदित निर्माता",
                bn = "অ্যালেম্বিক / সিপলা লি. / পিএমবিজেপি জনঔষধি অনুমোদিত প্রস্তুতকারক"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Treatment of susceptible bacterial infections including respiratory tract, throat, ear, skin, and certain typhoid/enteric infections.",
                    hi = "श्वसन तंत्र, गले, कान, त्वचा और कुछ आंत्र जीवाणु संक्रमणों के उपचार के लिए।",
                    bn = "শ্বাসনালী, গলা, কান, ত্বক এবং নির্দিষ্ট ব্যাকটেরিয়া সংক্রমণের চিকিৎসায় ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Macrolide antibiotic listed in NLEM 2022; targeted bacterial eradication when prescribed after clinical evaluation. Not effective against viral colds or flu.",
                    hi = "NLEM 2022 में शामिल मैक्रोलाइड एंटीबायोटिक; डॉक्टर की जांच के बाद जीवाणु संक्रमण के लिए प्रभावी। वायरल जुकाम पर असर नहीं करती।",
                    bn = "NLEM 2022 তালিকাভুক্ত ম্যাক্রোলাইড অ্যান্টিবায়োটিক; চিকিৎসকের পরামর্শে ব্যাকটেরিয়া সংক্রমণ দূর করে। ভাইরাল সর্দি-কাশিতে কার্যকর নয়।"
                ),
                howItWorks = LocalizedText(
                    en = "Binds to the 50S ribosomal subunit of susceptible bacteria, inhibiting bacterial protein synthesis and stopping bacterial growth.",
                    hi = "संवेदनशील बैक्टीरिया की 50S राइबोसोमल इकाई से जुड़कर प्रोटीन निर्माण रोकता है और बैक्टीरिया की वृद्धि थामता है।",
                    bn = "ব্যাকটেরিয়ার ৫০এস রাইবোজোমাল সাবইউনিটে যুক্ত হয়ে প্রোটিন সংশ্লেষণ বন্ধ করে ও জীবাণুর বৃদ্ধি রোধ করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Nausea, loose stools or mild diarrhea, abdominal cramping, mild headache.",
                    hi = "मतली, पतला मल या हल्का दस्त, पेट में मरोड़, हल्का सिरदर्द।",
                    bn = "বমি ভাব, পাতলা পায়খানা, পেটে খিঁচুনি, হালকা মাথাব্যথা।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "QT interval prolongation / irregular heartbeat, severe watery diarrhea (C. difficile), acute liver toxicity (jaundice), anaphylaxis or severe skin rash.",
                    hi = "अनियमित हृदय गति (QT प्रोलॉन्गेशन), गंभीर पानी जैसा दस्त, पीलिया/लिवर क्षति, गंभीर एलर्जी या त्वचा पर चकत्ते।",
                    bn = "হৃৎস্পন্দনের ছন্দপতন (QT প্রোলংগেশন), তীব্র জলবৎ ডায়রিয়া, জন্ডিস বা লিভারের সমস্যা, গুরুতর অ্যালার্জি।"
                ),
                warnings = LocalizedText(
                    en = "Prescription antibiotic: complete the prescribed duration to prevent antimicrobial resistance. Caution in patients with cardiac arrhythmia history or myasthenia gravis.",
                    hi = "प्रिस्क्रिप्शन एंटीबायोटिक: प्रतिरोध रोकने के लिए निर्धारित कोर्स पूरा करें। हृदय अतालता या मायस्थेनिया ग्रेविस में सावधानी रखें।",
                    bn = "প্রেসক্রিপশন অ্যান্টিবায়োটিক: অ্যান্টিবায়োটিক রেজিস্ট্যান্স এড়াতে সম্পূর্ণ কোর্স শেষ করুন। হৃদরোগ বা মায়াস্থেনিয়া গ্র্যাভিসে সতর্কতা প্রয়োজন।"
                ),
                drugInteractions = LocalizedText(
                    en = "Amiodarone, quinidine, sotalol (QT prolongation), warfarin, digoxin, colchicine, and aluminium/magnesium antacids (separate dosing).",
                    hi = "एमियोडेरोन, क्विनिडाइन, सोटालोल (हृदय लय जोखिम), वारफेरिन, डिगॉक्सिन और एल्युमीनियम/मैग्नीशियम एंटासिड।",
                    bn = "অ্যামিওডেরন, কুইনিডিন, সোটালল, ওয়ারফারিন, ডিগক্সিন ও অ্যান্টাসিডের সাথে ব্যবধান রেখে সেবনীয়।"
                ),
                contraindications = LocalizedText(
                    en = "Hypersensitivity to azithromycin, erythromycin, or macrolide antibiotics; history of cholestatic jaundice/hepatic dysfunction with prior azithromycin use.",
                    hi = "एज़िथ्रोमाइसिन या मैक्रोलाइड एंटीबायोटिक्स से एलर्जी; पूर्व में एज़िथ्रोमाइसिन से पीलिया या लिवर समस्या का इतिहास।",
                    bn = "ম্যাক্রোলাইড অ্যান্টিবায়োটিকে অ্যালার্জি বা পূর্বে অ্যাজিথ্রোমাইসিন ব্যবহারে জন্ডিস/লিভারের সমস্যা হয়ে থাকলে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Use during pregnancy or lactation only when clearly needed and prescribed by a physician after weighing risks and benefits.",
                    hi = "गर्भावस्था या स्तनपान के दौरान केवल डॉक्टर द्वारा स्पष्ट रूप से आवश्यक होने पर ही उपयोग करें।",
                    bn = "গর্ভাবস্থা বা স্তন্যদানকালে কেবল চিকিৎসকের সুস্পষ্ট নির্দেশনা অনুযায়ী ব্যবহারযোগ্য।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in original blister strip away from humidity and sunlight.",
                    hi = "30°C से कम तापमान पर मूल ब्लिस्टर पैक में नमी व धूप से दूर रखें।",
                    bn = "৩০°সে-এর নিচে মূল ব্লিস্টার প্যাকে শুষ্ক ও ঠান্ডা স্থানে রাখুন।"
                )
            ),
            prescriptionRequired = true,
            officialSources = "CDSCO Schedule H, NLEM 2022, U.S. FDA OpenFDA Azithromycin Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "azithromycin",
            popularScore = 910L,
            published = true
        ),
        Medicine(
            id = "metformin_500",
            name = LocalizedText(
                en = "Metformin 500 mg SR Tablet",
                hi = "मेटफॉर्मिन 500 mg SR टैबलेट",
                bn = "মেটফরমিন ৫০০ মিগ্রা এসআর ট্যাবলেট"
            ),
            brandName = "Glycomet-SR 500 / Glucophage / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Metformin Hydrochloride",
                hi = "मेटफॉर्मिन हाइड्रोक्लोराइड",
                bn = "মেটফরমিন হাইড্রোক্লোরাইড"
            ),
            saltComposition = "Metformin Hydrochloride IP 500 mg (Sustained Release)",
            categoryId = "diabetes",
            manufacturerId = "mfr_usv",
            manufacturerDetails = LocalizedText(
                en = "USV Private Ltd. (Mumbai) / Sun Pharma / PMBJP Jan Aushadhi",
                hi = "यूएसवी प्राइवेट लि. (मुंबई) / सन फार्मा / पीएमबीजेपी जन औषधि",
                bn = "ইউএসভি প্রা. লি. (মুম্বাই) / সান ফার্মা / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "First-line oral medication to control blood glucose in adults with Type 2 diabetes mellitus alongside diet and exercise.",
                    hi = "टाइप 2 मधुमेह में आहार और व्यायाम के साथ रक्त शर्करा नियंत्रित करने की प्रथम-पंक्ति दवा।",
                    bn = "টাইপ-২ ডায়াবেটিসে খাদ্যনিয়ন্ত্রণ ও ব্যায়ামের পাশাপাশি রক্তে শর্করা নিয়ন্ত্রণের প্রথম সারির ওষুধ।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Lowers fasting and post-meal blood glucose without causing weight gain or hypoglycemia when used alone; supports long-term microvascular risk reduction.",
                    hi = "वजन बढ़ाए बिना या अकेले लेने पर निम्न शर्करा किए बिना फास्टिंग व भोजनोत्तर शुगर नियंत्रित करती है।",
                    bn = "এককভাবে হাইপোগ্লাইসেমিয়া বা ওজন বৃদ্ধি না ঘটিয়েই রক্তের গ্লুকোজ নিয়ন্ত্রণে সহায়তা করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Decreases hepatic glucose production (gluconeogenesis), reduces intestinal glucose absorption, and improves peripheral insulin sensitivity.",
                    hi = "लिवर द्वारा ग्लूकोज उत्पादन कम करता है, आंतों से अवशोषण घटाता है और इंसुलिन संवेदनशीलता बढ़ाता है।",
                    bn = "লিভারে গ্লুকোজ উৎপাদন কমায়, অন্ত্র থেকে গ্লুকোজ শোষণ হ্রাস করে এবং ইনসুলিন সংবেদনশীলতা বাড়ায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Stomach upset, nausea, metallic taste, mild diarrhea, abdominal bloating (often reduced by taking with meals).",
                    hi = "पेट खराब होना, मतली, धातु जैसा स्वाद, हल्का दस्त, पेट फूलना (भोजन के साथ लेने पर कम होता है)।",
                    bn = "পেট খারাপ, বমি ভাব, মুখে ধাতব স্বাদ, হালকা ডায়রিয়া, পেট ফাঁপা (খাবারের সাথে খেলে কমে)।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Rare but life-threatening lactic acidosis (rapid breathing, severe muscle pain, extreme fatigue, cold feeling), vitamin B12 deficiency on long-term use.",
                    hi = "दुर्लभ लेकिन गंभीर लैक्टिक एसिडोसिस (तेज सांस, तीव्र मांसपेशी दर्द, अत्यधिक थकान), लंबे समय में विटामिन B12 की कमी।",
                    bn = "বিরল কিন্তু মারাত্মক ল্যাকটিক অ্যাসিডোসিস (দ্রুত শ্বাস, তীব্র পেশী ব্যথা, অতিরিক্ত দুর্বলতা), দীর্ঘমেয়াদে ভিটামিন বি১২ ঘাটতি।"
                ),
                warnings = LocalizedText(
                    en = "Swallow sustained-release tablets whole; do not crush or chew. Periodic kidney function (eGFR) and vitamin B12 checks are advised by physicians.",
                    hi = "SR टैबलेट को पूरा निगलें, चबाएं या कुचलें नहीं। डॉक्टर की सलाह से किडनी जांच (eGFR) व B12 जांच कराते रहें।",
                    bn = "এসআর ট্যাবলেট ভেঙে বা চিবিয়ে খাবেন না। চিকিৎসকের পরামর্শে নিয়মিত কিডনির কার্যকারিতা ও বি১২ পরীক্ষা করান।"
                ),
                drugInteractions = LocalizedText(
                    en = "Iodinated contrast media (may need temporary hold per physician), excessive alcohol, cimetidine, dolutegravir, carbonic anhydrase inhibitors, and diuretics.",
                    hi = "आयोडीन कंट्रास्ट डाई (जांच से पहले डॉक्टर से पूछें), अधिक शराब, सिमेटिडाइन, डोल्यूटेग्राविर और मूत्रवर्धक दवाएं।",
                    bn = "আয়োডিনযুক্ত কনট্রাস্ট ডাই, অতিরিক্ত অ্যালকোহল, সিমেটিডিন, ডোলুটেগ্রাভির ও ডায়ুরেটিকের সাথে সতর্কতা।"
                ),
                contraindications = LocalizedText(
                    en = "Severe renal impairment (eGFR < 30 mL/min/1.73m²), acute or chronic metabolic acidosis including diabetic ketoacidosis.",
                    hi = "गंभीर गुर्दा रोग (eGFR < 30), डायबिटिक कीटोएसिडोसिस या चयापचय एसिडोसिस।",
                    bn = "গুরুতর কিডনি অকার্যকারিতা (eGFR < ৩০) এবং ডায়াবেটিক কিটোঅ্যাসিডোসিসে সম্পূর্ণ নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Consult an endocrinologist/obstetrician during pregnancy or breastfeeding; insulin is often preferred or adjustments are closely monitored.",
                    hi = "गर्भावस्था या स्तनपान के दौरान डॉक्टर से परामर्श लें; अक्सर इंसुलिन या सख्त चिकित्सकीय निगरानी आवश्यक होती है।",
                    bn = "গর্ভাবস্থা ও স্তন্যদানে চিকিৎসকের পরামর্শ আবশ্যক; অনেক ক্ষেত্রে ইনসুলিন বা নিবিড় পর্যবেক্ষণ প্রয়োজন হয়।"
                ),
                storageInstructions = LocalizedText(
                    en = "Keep tightly sealed below 30°C in a moisture-free container away from children.",
                    hi = "30°C से कम तापमान पर सूखे बंद पैक में बच्चों की पहुंच से दूर रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক ও বায়ুরোধী স্থানে শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = true,
            officialSources = "CDSCO Schedule H, NLEM 2022, U.S. FDA OpenFDA Metformin Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "metformin",
            popularScore = 940L,
            published = true
        ),
        Medicine(
            id = "amlodipine_5",
            name = LocalizedText(
                en = "Amlodipine 5 mg Tablet",
                hi = "एम्लोडिपाइन 5 mg टैबलेट",
                bn = "অ্যামলোডিপিন ৫ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Amlong 5 / Stamlo 5 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Amlodipine Besilate",
                hi = "एम्लोडिपाइन बेसिलेट",
                bn = "অ্যামলোডিপিন বেসিলেট"
            ),
            saltComposition = "Amlodipine Besilate IP eq. to Amlodipine 5 mg",
            categoryId = "blood_pressure",
            manufacturerId = "mfr_micro",
            manufacturerDetails = LocalizedText(
                en = "Micro Labs Ltd. / Dr. Reddy's / PMBJP Jan Aushadhi",
                hi = "माइक्रो लैब्स लि. / डॉ. रेड्डीज / पीएमबीजेपी जन औषधि",
                bn = "মাইক্রো ল্যাবস লি. / ড. রেড্ডিজ / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Management of high blood pressure (hypertension) and chronic stable or vasospastic angina (chest pain).",
                    hi = "उच्च रक्तचाप (हाइपरटेंशन) और क्रॉनिक एनजाइना (सीने के दर्द) के प्रबंधन के लिए।",
                    bn = "উচ্চ রক্তচাপ (হাইপারটেনশন) এবং অ্যানজাইনা (বুকব্যথা) নিয়ন্ত্রণে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Once-daily long-acting calcium channel blocker on NLEM 2022 that lowers cardiovascular strain, stroke, and heart-attack risk.",
                    hi = "NLEM 2022 में शामिल दिन में एक बार ली जाने वाली दवा जो रक्तचाप नियंत्रित कर स्ट्रोक व हृदय रोग जोखिम घटाती है।",
                    bn = "NLEM 2022 তালিকাভুক্ত দীর্ঘমেয়াদী ক্যালসিয়াম চ্যানেল ব্লকার যা স্ট্রোক ও হার্ট অ্যাটাকের ঝুঁকি কমাতে সাহায্য করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Relaxes vascular smooth muscle by inhibiting calcium ion influx into arterial smooth muscle and cardiac muscle, lowering peripheral resistance.",
                    hi = "रक्त वाहिकाओं की मांसपेशियों में कैल्शियम प्रवेश को रोककर उन्हें शिथिल करता है, जिससे रक्त प्रवाह आसान होता है।",
                    bn = "রক্তনালির মসৃণ পেশীতে ক্যালসিয়াম প্রবেশ কমিয়ে রক্তনালি প্রসারিত করে ও রক্তচাপ হ্রাস করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Swelling of ankles or feet (peripheral edema), flushing, mild dizziness, palpitations, fatigue.",
                    hi = "टखनों या पैरों में सूजन, चेहरे पर लालिमा, हल्का चक्कर, दिल की धड़कन तेज महसूस होना, थकान।",
                    bn = "গোড়ালি বা পা ফুলে যাওয়া, মুখ লাল হওয়া, হালকা মাথা ঘোরা, বুক ধড়ফড় করা, ক্লান্তি।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Severe hypotension (fainting), worsening chest pain upon starting in severe obstructive coronary disease, severe allergic swelling.",
                    hi = "गंभीर निम्न रक्तचाप (बेहोशी), गंभीर कोरोनरी रोग में शुरुआती सीने का दर्द बढ़ना, गंभीर एलर्जी सूजन।",
                    bn = "তীব্র নিম্ন রক্তচাপ বা অজ্ঞান হওয়া, শুরুতে বুকব্যথা বেড়ে যাওয়া, তীব্র অ্যালার্জিক ফোলাভাব।"
                ),
                warnings = LocalizedText(
                    en = "Do not stop blood pressure medication suddenly without consulting your doctor. Caution in severe aortic stenosis and severe hepatic impairment.",
                    hi = "डॉक्टर से पूछे बिना बीपी की दवा अचानक बंद न करें। गंभीर लिवर रोग या एओर्टिक स्टेनोसिस में सावधानी आवश्यक है।",
                    bn = "চিকিৎসকের পরামর্শ ছাড়া হঠাৎ রক্তচাপের ওষুধ বন্ধ করবেন না। লিভারের জটিল রোগে বিশেষ সতর্কতা প্রয়োজন।"
                ),
                drugInteractions = LocalizedText(
                    en = "Strong CYP3A4 inhibitors (ketoconazole, clarithromycin, ritonavir), simvastatin (>20 mg/day limit), sildenafil (additive BP drop), tacrolimus.",
                    hi = "क्लैरिथ्रोमाइसिन, कीटोकोनाज़ोल, सिमवास्टेटिन (खुराक सीमा), सिल्डेनाफिल (बीपी में अत्यधिक गिरावट) और टैक्रोलिमस।",
                    bn = "ক্ল্যারিথ্রোমাইসিন, কিটোকোনাজল, সিমভাস্ট্যাটিন, সিলডেনাফিল ও ট্যাক্রোলিমাসের সাথে সতর্কতা।"
                ),
                contraindications = LocalizedText(
                    en = "Known hypersensitivity to dihydropyridines / amlodipine, cardiogenic shock, severe symptomatic hypotension.",
                    hi = "एम्लोडिपाइन या डाइहाइड्रोपाइरीडीन से एलर्जी, कार्डियोजेनिक शॉक या गंभीर निम्न रक्तचाप।",
                    bn = "অ্যামলোডিপিনে অতিসংবেদনশীলতা, কার্ডিওজেনিক শক বা মারাত্মক নিম্ন রক্তচাপে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Inform your doctor if pregnant, planning pregnancy, or breastfeeding; specialist evaluation is required for antihypertensive choice.",
                    hi = "गर्भावस्था या स्तनपान की स्थिति में डॉक्टर को बताएं; बीपी दवा के चयन हेतु विशेषज्ञ सलाह आवश्यक है।",
                    bn = "গর্ভবতী বা স্তন্যদানকারী হলে চিকিৎসককে জানান; নিরাপদ রক্তচাপ নিয়ন্ত্রণের জন্য বিশেষজ্ঞ পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in a dry light-resistant strip away from children.",
                    hi = "30°C से कम तापमान पर सूखे व प्रकाश से सुरक्षित पैक में रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক ও আলো-নিরোধক প্যাকে শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = true,
            officialSources = "CDSCO Schedule H, NLEM 2022, U.S. FDA OpenFDA Amlodipine Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "amlodipine",
            popularScore = 900L,
            published = true
        ),
        Medicine(
            id = "atorvastatin_10",
            name = LocalizedText(
                en = "Atorvastatin 10 mg Tablet",
                hi = "एटोरवास्टेटिन 10 mg टैबलेट",
                bn = "অ্যাটোরভাস্ট্যাটিন ১০ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Atorva 10 / Storvas 10 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Atorvastatin",
                hi = "एटोरवास्टेटिन",
                bn = "অ্যাটোরভাস্ট্যাটিন"
            ),
            saltComposition = "Atorvastatin Calcium IP eq. to Atorvastatin 10 mg",
            categoryId = "heart_care",
            manufacturerId = "mfr_sun",
            manufacturerDetails = LocalizedText(
                en = "Zydus / Sun Pharmaceutical / PMBJP Jan Aushadhi",
                hi = "ज़ाइडस / सन फार्मा / पीएमबीजेपी जन औषधि",
                bn = "জাইডাস / সান ফার্মা / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Lowers elevated LDL cholesterol and triglycerides and reduces risk of heart attack and stroke in high-risk adults.",
                    hi = "बढ़े हुए LDL कोलेस्ट्रॉल और ट्राइग्लिसराइड्स को कम करने तथा हार्ट अटैक व स्ट्रोक का जोखिम घटाने के लिए।",
                    bn = "রক্তে অতিরিক্ত এলডিএল কোলেস্টেরল ও ট্রাইগ্লিসারাইড কমাতে এবং হার্ট অ্যাটাক ও স্ট্রোকের ঝুঁকি কমাতে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Cardioprotective HMG-CoA reductase inhibitor included in NLEM 2022 for lipid control and primary/secondary cardiovascular prevention.",
                    hi = "हृदय सुरक्षा हेतु NLEM 2022 में शामिल स्टैटिन दवा जो धमनियों में प्लाक जमाव जोखिम कम करती है।",
                    bn = "NLEM 2022 তালিকাভুক্ত স্ট্যাটিন যা ধমনীতে চর্বি জমা প্রতিরোধে গুরুত্বপূর্ণ ভূমিকা রাখে।"
                ),
                howItWorks = LocalizedText(
                    en = "Competitively inhibits HMG-CoA reductase in the liver, decreasing endogenous cholesterol synthesis and increasing hepatic LDL clearance.",
                    hi = "लिवर में HMG-CoA रिडक्टेस एंजाइम को अवरुद्ध कर कोलेस्ट्रॉल निर्माण कम करता है और LDL क्लीयरेंस बढ़ाता है।",
                    bn = "লিভারে HMG-CoA রিডাক্টেজ এনজাইম ব্লক করে কোলেস্টেরল তৈরি কমায় ও রক্ত থেকে খারাপ কোলেস্টেরল সরায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Mild muscle soreness, joint pain, mild indigestion, nasopharyngitis, mild diarrhea.",
                    hi = "हल्का मांसपेशी दर्द, जोड़ों में दर्द, हल्की अपच, नाक-गले में खराश, हल्का दस्त।",
                    bn = "হালকা পেশী ব্যথা, অস্থিসন্ধিতে ব্যথা, সামান্য বদহজম, সর্দি-গলা ব্যথা।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Myopathy / rhabdomyolysis (unexplained severe muscle pain, tenderness, dark tea-colored urine), elevated liver transaminases, rare new-onset hyperglycemia.",
                    hi = "गंभीर मांसपेशी क्षति (रैबडोमायोलिसिस — तेज दर्द, गहरा भूरा मूत्र), लिवर एंजाइम बढ़ना।",
                    bn = "গুরুতর পেশী ক্ষয় (র‍্যাবডোমায়োলাইসিস — তীব্র পেশী ব্যথা ও গাঢ় প্রস্রাব), লিভার এনজাইম বৃদ্ধি।"
                ),
                warnings = LocalizedText(
                    en = "Report unexplained muscle pain, weakness, or dark urine to your doctor promptly. Avoid large quantities of grapefruit juice and heavy alcohol use.",
                    hi = "अस्पष्ट मांसपेशी दर्द, कमजोरी या गहरे रंग के मूत्र पर तुरंत डॉक्टर से संपर्क करें। अधिक शराब व अंगूर के रस से बचें।",
                    bn = "অকারণ পেশী ব্যথা, দুর্বলতা বা গাঢ় রঙের প্রস্রাব হলে দ্রুত চিকিৎসককে জানান। অতিরিক্ত অ্যালকোহল পরিহার করুন।"
                ),
                drugInteractions = LocalizedText(
                    en = "Cyclosporine, gemfibrozil, clarithromycin, itraconazole, HIV/HCV protease inhibitors, colchicine, and high-dose niacin.",
                    hi = "साइक्लोस्पोरिन, जेमफाइब्रोज़िल, क्लैरिथ्रोमाइसिन, इट्राकोनाज़ोल, कोल्चिसिन और उच्च खुराक नियासिन।",
                    bn = "সাইক্লোস্পোরিন, জেমফিব্রোজিল, ক্ল্যারিথ্রোমাইসিন, ইট্রাকোনাজল ও কলচিসিনের সাথে ওষুধের মিথস্ক্রিয়া হতে পারে।"
                ),
                contraindications = LocalizedText(
                    en = "Active liver disease, unexplained persistent elevations of serum transaminases, hypersensitivity, pregnancy and breastfeeding.",
                    hi = "सक्रिय लिवर रोग, लगातार बढ़े लिवर एंजाइम, एलर्जी, गर्भावस्था और स्तनपान।",
                    bn = "সক্রিয় লিভারের রোগ, অকারণ লিভার এনজাইম বৃদ্ধি, গর্ভাবস্থা ও স্তন্যদানকালে নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Contraindicated in pregnancy and breastfeeding; cholesterol synthesis is essential for fetal development. Discontinue immediately if pregnancy occurs and consult your doctor.",
                    hi = "गर्भावस्था और स्तनपान में पूर्णतः वर्जित है। गर्भधारण होने पर तुरंत डॉक्टर से संपर्क करें।",
                    bn = "গর্ভাবস্থা এবং স্তন্যদানকালে সম্পূর্ণ নিষিদ্ধ; গর্ভধারণ করলে দ্রুত চিকিৎসকের পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 25–30°C in dry original packaging away from moisture and children.",
                    hi = "25–30°C से कम तापमान पर सूखे मूल पैक में बच्चों की पहुंच से दूर रखें।",
                    bn = "২৫–৩০°সে-এর নিচে শুষ্ক ও মূল প্যাকে শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = true,
            officialSources = "CDSCO Schedule H, NLEM 2022, U.S. FDA OpenFDA Atorvastatin Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "atorvastatin",
            popularScore = 880L,
            published = true
        ),
        Medicine(
            id = "cholecalciferol_60k",
            name = LocalizedText(
                en = "Cholecalciferol (Vitamin D3) 60,000 IU",
                hi = "कोलेकैल्सिफेरॉल (विटामिन D3) 60,000 IU",
                bn = "কোলেক্যালসিফেরল (ভিটামিন ডি৩) ৬০,০০০ আইইউ"
            ),
            brandName = "Uprise-D3 60K / D-Rise 60K / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Cholecalciferol (Vitamin D3)",
                hi = "कोलेकैल्सिफेरॉल (विटामिन D3)",
                bn = "কোলেক্যালসিফেরল (ভিটামিন ডি৩)"
            ),
            saltComposition = "Cholecalciferol (Vitamin D3) IP 60,000 IU",
            categoryId = "vitamins",
            manufacturerId = "mfr_alkem",
            manufacturerDetails = LocalizedText(
                en = "Alkem Laboratories Ltd. / USV / PMBJP Jan Aushadhi",
                hi = "अल्केम लैबोरेटरीज लि. / यूएसवी / पीएमबीजेपी जन औषधि",
                bn = "অ্যালকেম ল্যাবরেটরিজ লি. / ইউএসভি / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Correction and maintenance of Vitamin D3 deficiency, supporting calcium absorption, bone mineralization, and musculoskeletal strength.",
                    hi = "विटामिन D3 की कमी को दूर करने, कैल्शियम अवशोषण और हड्डियों की मजबूती के लिए।",
                    bn = "ভিটামিন ডি৩-এর ঘাটতি পূরণ, ক্যালসিয়াম শোষণ ও হাড়ের মজবুতির জন্য ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Weekly high-dose supplementation helps restore serum 25-hydroxyvitamin D levels in osteomalacia, rickets, and osteoporosis adjunct therapy.",
                    hi = "साप्ताहिक उच्च खुराक विटामिन D3 स्तर बहाल कर हड्डियों की कमजोरी और ऑस्टियोपोरोसिस प्रबंधन में मदद करती है।",
                    bn = "সাপ্তাহিক উচ্চ মাত্রার সাপ্লিমেন্ট রক্তে ভিটামিন ডি-এর মাত্রা স্বাভাবিক করে হাড়ের স্বাস্থ্য রক্ষা করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Converted in liver and kidneys to active calcitriol, which promotes intestinal calcium and phosphate absorption and bone mineralization.",
                    hi = "लिवर व किडनी में सक्रिय कैल्सिट्रायोल में बदलकर आंतों से कैल्शियम और फॉस्फेट अवशोषण बढ़ाता है।",
                    bn = "লিভার ও কিডনিতে সক্রিয় ক্যালসিট্রায়োলে রূপান্তরিত হয়ে অন্ত্র থেকে ক্যালসিয়াম ও ফসফেট শোষণ বাড়ায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Usually well tolerated at weekly prescribed schedule; occasional mild constipation or nausea if taken with excess calcium.",
                    hi = "साप्ताहिक निर्धारित खुराक पर सामान्यतः सुरक्षित; कभी-कभी हल्की कब्ज या मतली।",
                    bn = "নির্ধারিত সাপ্তাহিক মাত্রায় সাধারণত সহনীয়; মাঝে মাঝে হালকা কোষ্ঠকাঠিন্য বা বমি ভাব হতে পারে।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Hypervitaminosis D & hypercalcemia from excessive dosing (persistent vomiting, excessive thirst, frequent urination, kidney stones, confusion).",
                    hi = "अत्यधिक खुराक से हाइपरविटामिनोसिस D व उच्च कैल्शियम (लगातार उल्टी, अत्यधिक प्यास, बार-बार पेशाब, गुर्दे की पथरी)।",
                    bn = "অতিরিক্ত মাত্রায় হাইপারভিটামিনোসিস ডি ও হাইপারক্যালসেমিয়া (ক্রমাগত বমি, অতিরিক্ত তৃষ্ণা, কিডনিতে পাথর)।"
                ),
                warnings = LocalizedText(
                    en = "High-strength 60,000 IU is NOT a daily supplement — follow exact weekly/monthly frequency advised by your physician. Take after a meal for better absorption.",
                    hi = "60,000 IU दैनिक गोली नहीं है — डॉक्टर द्वारा बताई साप्ताहिक/मासिक आवृत्ति का ही पालन करें। भोजन के बाद लें।",
                    bn = "৬০,০০০ আইইউ প্রতিদিন খাওয়ার ওষুধ নয় — চিকিৎসকের নির্ধারিত সাপ্তাহিক বা মাসিক নিয়ম অনুসরণ করুন। খাবারের পর সেবন ভালো।"
                ),
                drugInteractions = LocalizedText(
                    en = "Thiazide diuretics (hypercalcemia risk), digoxin (arrhythmia risk with high calcium), orlistat/cholestyramine (reduced absorption), phenytoin.",
                    hi = "थायज़ाइड मूत्रवर्धक, डिगॉक्सिन, ऑर्लिस्टैट/कोलेस्टाइरामिन (कम अवशोषण) और फेनिटोइन।",
                    bn = "থায়াজাইড ডায়ুরেটিক, ডিগক্সিন, অরলিস্ট্যাট ও ফেনাইটয়েনের সাথে সতর্কতা।"
                ),
                contraindications = LocalizedText(
                    en = "Hypercalcemia, hypervitaminosis D, malabsorption syndrome, severe renal osteodystrophy with hyperphosphatemia.",
                    hi = "हाइपरकैल्सीमिया (रक्त में उच्च कैल्शियम), विटामिन D विषाक्तता या इससे एलर्जी।",
                    bn = "হাইপারক্যালসেমিয়া, ভিটামিন ডি টক্সিসিটি বা অতিসংবেদনশীলতায় নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Adequate Vitamin D is important in pregnancy/lactation, but 60,000 IU high-dose schedules require obstetrician evaluation.",
                    hi = "गर्भावस्था व स्तनपान में विटामिन D आवश्यक है, पर 60,000 IU जैसी उच्च खुराक डॉक्टर की सलाह से ही लें।",
                    bn = "গর্ভাবস্থায় ভিটামিন ডি প্রয়োজন হলেও ৬০,০০০ আইইউ উচ্চ মাত্রার ক্ষেত্রে স্ত্রীরোগ বিশেষজ্ঞের পরামর্শ আবশ্যক।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 25°C protected from direct heat and light; do not freeze softgels.",
                    hi = "25°C से कम तापमान पर गर्मी और रोशनी से दूर रखें; सॉफ्टजेल जमाएं नहीं।",
                    bn = "২৫°সে-এর নিচে ঠান্ডা ও আলো থেকে দূরে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO Nutrition & Vitamin Guidelines, NLEM 2022, U.S. FDA Cholecalciferol Reference",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "cholecalciferol",
            popularScore = 860L,
            published = true
        ),
        Medicine(
            id = "pantoprazole_40",
            name = LocalizedText(
                en = "Pantoprazole 40 mg Tablet",
                hi = "पैंटोप्राज़ोल 40 mg टैबलेट",
                bn = "প্যান্টোপ্রাজল ৪০ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Pan 40 / Pantocid 40 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Pantoprazole Sodium",
                hi = "पैंटोप्राज़ोल सोडियम",
                bn = "প্যান্টোপ্রাজল সোডিয়াম"
            ),
            saltComposition = "Pantoprazole Sodium IP eq. to Pantoprazole 40 mg",
            categoryId = "digestive",
            manufacturerId = "mfr_alkem",
            manufacturerDetails = LocalizedText(
                en = "Alkem Laboratories Ltd. / Sun Pharma / PMBJP Jan Aushadhi",
                hi = "अल्केम लैबोरेटरीज लि. / सन फार्मा / पीएमबीजेपी जन औषधि",
                bn = "অ্যালকেম ল্যাবরেটরিজ লি. / সান ফার্মা / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Treatment of gastroesophageal reflux disease (GERD), acid reflux, peptic ulcers, and Zollinger-Ellison syndrome.",
                    hi = "एसिड रिफ्लक्स (GERD), सीने में जलन, पेप्टिक अल्सर और अत्यधिक पेट के एसिड के उपचार के लिए।",
                    bn = "অ্যাসিড রিফ্লাক্স (GERD), বুকজ্বালা, পেপটিক আলসার এবং অতিরিক্ত গ্যাস্ট্রিক অ্যাসিড নিয়ন্ত্রণে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Proton pump inhibitor providing sustained 24-hour acid suppression to allow esophageal and gastric mucosal healing.",
                    hi = "प्रोटॉन पंप इनहिबिटर जो 24 घंटे तक पेट का एसिड कम कर अन्नप्रणाली व पेट की परत को ठीक होने में मदद करता है।",
                    bn = "প্রোটন পাম্প ইনহিবিটর যা দীর্ঘ সময় অ্যাসিড নিঃসরণ কমিয়ে আলসার ও খাদ্যনালীর ক্ষত সারাতে সাহায্য করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Irreversibly blocks the H+/K+ ATPase enzyme system (proton pump) on gastric parietal cells, suppressing the final step of acid secretion.",
                    hi = "पेट की पैराइटल कोशिकाओं में H+/K+ ATPase प्रोटॉन पंप को अवरुद्ध कर एसिड स्राव को रोकता है।",
                    bn = "পাকস্থলীর প্যারাইটাল কোষের H+/K+ ATPase প্রোটন পাম্প বন্ধ করে অ্যাসিড তৈরি নিয়ন্ত্রণ করে।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Headache, mild diarrhea, nausea, abdominal gas, dizziness.",
                    hi = "सिरदर्द, हल्का दस्त, मतली, पेट में गैस, चक्कर आना।",
                    bn = "মাথাব্যথা, হালকা ডায়রিয়া, বমি ভাব, পেটে গ্যাস, মাথা ঘোরা।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Long-term use risks: hypomagnesemia, vitamin B12 deficiency, bone fracture risk, acute interstitial nephritis, C. difficile diarrhea.",
                    hi = "लंबे समय तक उपयोग के जोखिम: मैग्नीशियम व विटामिन B12 की कमी, हड्डी फ्रैक्चर जोखिम, गुर्दे की सूजन।",
                    bn = "দীর্ঘমেয়াদে ব্যবহারের ঝুঁকি: ম্যাগনেসিয়াম ও বি১২ ঘাটতি, হাড় দুর্বল হওয়া, কিডনির প্রদাহ বা গুরুতর ডায়রিয়া।"
                ),
                warnings = LocalizedText(
                    en = "Swallow gastro-resistant tablet whole 30–60 minutes before breakfast. Do not self-medicate continuously for chronic symptoms without ruling out serious gastric illness.",
                    hi = "गैस्ट्रो-रेसिस्टेंट टैबलेट को नाश्ते से 30–60 मिनट पहले पूरा निगलें। लगातार स्वयं दवा न लें।",
                    bn = "অ্যান্টেরিক-কোটেড ট্যাবলেটটি নাস্তার ৩০–৬০ মিনিট আগে আস্ত গিলে খান। দীর্ঘদিন চিকিৎসকের পরামর্শ ছাড়া ব্যবহার করবেন না।"
                ),
                drugInteractions = LocalizedText(
                    en = "Methotrexate (high doses), warfarin, rilpivirine/atazanavir, ketoconazole, iron salts, and mycophenolate mofetil (reduced absorption).",
                    hi = "मेथोट्रेक्सेट, वारफेरिन, कीटोकोनाज़ोल, आयरन सप्लीमेंट और कुछ एंटी-वायरल दवाओं का अवशोषण प्रभावित होता है।",
                    bn = "মেথোট্রেক্সেট, ওয়ারফারিন, কিটোকোনাজল, আয়রন ও কিছু অ্যান্টিভাইরাল ওষুধের শোষণে প্রভাব ফেলে।"
                ),
                contraindications = LocalizedText(
                    en = "Hypersensitivity to pantoprazole or substituted benzimidazoles; co-administration with rilpivirine-containing regimens.",
                    hi = "पैंटोप्राज़ोल या बेंजिमिडाजोल वर्ग से एलर्जी; रिल्पिविरिन दवाओं के साथ सह-उपयोग।",
                    bn = "প্যান্টোপ্রাজলে অ্যালার্জি এবং রিলপিভিরিনযুক্ত ওষুধের সাথে ব্যবহার নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Use during pregnancy or breastfeeding only if prescribed by a physician after assessing expected benefits versus potential risks.",
                    hi = "गर्भावस्था व स्तनपान के दौरान केवल डॉक्टर के परामर्श से ही उपयोग करें।",
                    bn = "গর্ভাবস্থা ও স্তন্যদানকালে কেবল চিকিৎসকের পরামর্শ অনুযায়ী ব্যবহার করুন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in moisture-proof blister packaging away from children.",
                    hi = "30°C से कम तापमान पर नमीरोधी पैक में बच्चों से दूर रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক ব্লিস্টার প্যাকে শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = true,
            officialSources = "CDSCO Schedule H, NLEM 2022, U.S. FDA OpenFDA Pantoprazole Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "pantoprazole",
            popularScore = 895L,
            published = true
        ),
        Medicine(
            id = "cetirizine_10",
            name = LocalizedText(
                en = "Cetirizine 10 mg Tablet",
                hi = "सेटिरिज़ीन 10 mg टैबलेट",
                bn = "সেটিরিজিন ১০ মিগ্রা ট্যাবলেট"
            ),
            brandName = "Okacet 10 / Cetzine 10 / Jan Aushadhi",
            genericName = LocalizedText(
                en = "Cetirizine Hydrochloride",
                hi = "सेटिरिज़ीन हाइड्रोक्लोराइड",
                bn = "সেটিরিজিন হাইড্রোক্লোরাইড"
            ),
            saltComposition = "Cetirizine Hydrochloride IP 10 mg",
            categoryId = "allergy",
            manufacturerId = "mfr_cipla",
            manufacturerDetails = LocalizedText(
                en = "Cipla Ltd. / GSK / Dr. Reddy's / PMBJP Jan Aushadhi",
                hi = "सिप्ला लि. / जीएसके / डॉ. रेड्डीज / पीएमबीजेपी जन औषधि",
                bn = "সিপলা লি. / জিএসকে / ড. রেড্ডিজ / পিএমবিজেপি জনঔষধি"
            ),
            monograph = MedicineMonograph(
                usedFor = LocalizedText(
                    en = "Relief of allergic rhinitis symptoms (sneezing, runny nose, itchy watery eyes) and chronic idiopathic urticaria (hives).",
                    hi = "एलर्जिक राइनाइटिस (छींक, बहती नाक, आंखों में खुजली) और पित्ती (त्वचा एलर्जी) से राहत के लिए।",
                    bn = "অ্যালার্জিক রাইনাইটিস (হাঁচি, নাক দিয়ে জল পড়া, চোখ চুলকানো) ও ত্বকের চুলকানি/হাইভস উপশমে ব্যবহৃত।"
                ),
                usesAndBenefits = LocalizedText(
                    en = "Second-generation H1 antihistamine on NLEM 2022 providing 24-hour allergy relief with lower sedation than first-generation antihistamines.",
                    hi = "NLEM 2022 में शामिल द्वितीय-पीढ़ी एंटीहिस्टामाइन जो कम नींद के साथ 24 घंटे एलर्जी से राहत देती है।",
                    bn = "NLEM 2022 তালিকাভুক্ত দ্বিতীয় প্রজন্মের অ্যান্টিহিস্টামিন যা ২৪ ঘণ্টা অ্যালার্জি নিয়ন্ত্রণ করে।"
                ),
                howItWorks = LocalizedText(
                    en = "Selectively blocks peripheral H1 histamine receptors, preventing histamine-induced itching, sneezing, and mucosal swelling.",
                    hi = "परिधीय H1 हिस्टामाइन रिसेप्टर्स को ब्लॉक कर एलर्जी से होने वाली खुजली, छींक और सूजन रोकता है।",
                    bn = "পেরিফেরাল H1 হিস্টামিন রিসেপ্টর ব্লক করে অ্যালার্জিজনিত চুলকানি, হাঁচি ও ফোলাভাব কমায়।"
                ),
                commonSideEffects = LocalizedText(
                    en = "Drowsiness, dry mouth, mild fatigue, mild headache, stomach discomfort.",
                    hi = "हल्की नींद या सुस्ती, मुंह सूखना, थकान, हल्का सिरदर्द।",
                    bn = "তন্দ্রাভাব, মুখ শুকিয়ে যাওয়া, হালকা ক্লান্তি, সামান্য মাথাব্যথা।"
                ),
                seriousSideEffects = LocalizedText(
                    en = "Severe hypersensitivity/anaphylaxis (rare), urinary retention, severe dizziness, paradoxical agitation in children.",
                    hi = "दुर्लभ गंभीर एलर्जी प्रतिक्रिया, पेशाब रुकना, अत्यधिक चक्कर या बच्चों में बेचैनी।",
                    bn = "বিরল গুরুতর অ্যালার্জি, প্রস্রাব আটকে যাওয়া, তীব্র মাথা ঘোরা বা শিশুদের অস্থিরতা।"
                ),
                warnings = LocalizedText(
                    en = "May cause drowsiness in some people — exercise caution before driving or operating machinery. Dose reduction may be needed in kidney disease.",
                    hi = "कुछ लोगों को नींद आ सकती है — वाहन चलाने या मशीनरी उपयोग में सावधानी बरतें। गुर्दा रोग में खुराक समायोजन आवश्यक हो सकता है।",
                    bn = "তন্দ্রাভাব হতে পারে বলে গাড়ি বা ভারী যন্ত্র চালানোর আগে সতর্ক থাকুন। কিডনি রোগে মাত্রা কমানো লাগতে পারে।"
                ),
                drugInteractions = LocalizedText(
                    en = "Alcohol, CNS depressants (benzodiazepines, opioids, sedatives), and high-dose theophylline (slightly reduced cetirizine clearance).",
                    hi = "शराब, नींद या तंत्रिका तंत्र को सुस्त करने वाली दवाएं, और थियोफाइलाइन।",
                    bn = "অ্যালকোহল, ঘুমের ওষুধ বা সিএনএস ডিপ্রেস্যান্ট এবং থিওফাইলিনের সাথে তন্দ্রাভাব বাড়তে পারে।"
                ),
                contraindications = LocalizedText(
                    en = "Known hypersensitivity to cetirizine, hydroxyzine, or piperazine derivatives; severe renal failure (CrCl < 10 mL/min).",
                    hi = "सेटिरिज़ीन या हाइड्रोक्साइज़ीन से एलर्जी; गंभीर गुर्दा विफलता।",
                    bn = "সেটিরিজিন বা হাইড্রোক্সিজিনে অ্যালার্জি এবং গুরুতর কিডনি বিকল অবস্থায় নিষিদ্ধ।"
                ),
                pregnancyWarnings = LocalizedText(
                    en = "Consult a physician before use in pregnancy; excreted in small amounts in breast milk so medical advice is recommended while nursing.",
                    hi = "गर्भावस्था में उपयोग से पहले डॉक्टर से सलाह लें; स्तनपान में चिकित्सकीय परामर्श आवश्यक है।",
                    bn = "গর্ভাবস্থায় ও স্তন্যদানকালে ব্যবহারের আগে চিকিৎসকের পরামর্শ নিন।"
                ),
                storageInstructions = LocalizedText(
                    en = "Store below 30°C in a dry strip away from children.",
                    hi = "30°C से कम तापमान पर सूखे पैक में बच्चों से दूर रखें।",
                    bn = "৩০°সে-এর নিচে শুষ্ক স্থানে শিশুদের নাগালের বাইরে রাখুন।"
                )
            ),
            prescriptionRequired = false,
            officialSources = "CDSCO India, NLEM 2022, U.S. FDA OpenFDA Cetirizine Label",
            lastVerifiedDate = "2026-11-20",
            openFdaGenericQuery = "cetirizine",
            popularScore = 875L,
            published = true
        )
    )
}
