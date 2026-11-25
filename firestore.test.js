const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_999";
const ADMIN_EMAIL = "Samadconnecting@gmail.com";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function nowMinus(ms = 1000) {
  return new Date(Date.now() - ms);
}

function sampleMedicine(created, updated) {
  return {
    name: { en: "Paracetamol 500 mg", hi: "पैरासिटामोल 500 मिग्रा", bn: "প্যারাসিটামল ৫০০ মিগ্রা" },
    brandName: "Dolo 500 / Jan Aushadhi Paracetamol",
    genericName: { en: "Paracetamol (Acetaminophen)", hi: "पैरासिटामोल", bn: "প্যারাসিটামল" },
    saltComposition: "Paracetamol IP 500 mg",
    categoryId: "fever_pain",
    manufacturerId: "mfr_micro",
    manufacturerDetails: {
      en: "Micro Labs Ltd., Bengaluru, India",
      hi: "माइक्रो लैब्स लि., बेंगलुरु",
      bn: "মাইক্রো ল্যাবস লিমিটেড, বেঙ্গালুরু",
    },
    monograph: {
      usedFor: {
        en: "Fever and mild to moderate pain relief.",
        hi: "बुखार और हल्के से मध्यम दर्द से राहत।",
        bn: "জ্বর ও হালকা থেকে মাঝারি ব্যথা উপশমে ব্যবহৃত।",
      },
      usesAndBenefits: {
        en: "Reduces fever and relieves headache or body ache.",
        hi: "बुखार कम करता है और सिरदर्द से राहत देता है।",
        bn: "জ্বর কমায় এবং মাথাব্যথা উপশম করে।",
      },
      howItWorks: {
        en: "Inhibits prostaglandin synthesis in the central nervous system.",
        hi: "केंद्रीय तंत्रिका तंत्र में प्रोस्टाग्लैंडीन को रोकता है।",
        bn: "কেন্দ্রীয় স্নায়ুতন্ত্রে প্রোস্টাগ্ল্যান্ডিন সংশ্লেষ রোধ করে।",
      },
      commonSideEffects: {
        en: "Mild nausea, stomach upset.",
        hi: "हल्की मतली, पेट खराब होना।",
        bn: "হালকা বমি ভাব, পেটের অস্বস্তি।",
      },
      seriousSideEffects: {
        en: "Severe skin rash, liver injury on overdose, allergic swelling.",
        hi: "गंभीर त्वचा चकत्ते, अधिक मात्रा से लिवर क्षति।",
        bn: "তীব্র ত্বকের ফুসকুড়ি, অতিরিক্ত মাত্রায় লিভারের ক্ষতি।",
      },
      warnings: {
        en: "Do not exceed doctor-recommended daily limit. Avoid alcohol.",
        hi: "डॉक्टर की बताई सीमा से अधिक न लें। शराब से बचें।",
        bn: "চিকিৎসকের নির্ধারিত মাত্রার বেশি নেবেন না। অ্যালকোহল এড়িয়ে চলুন।",
      },
      drugInteractions: {
        en: "Warfarin, carbamazepine, other paracetamol-containing products.",
        hi: "वारफारिन, कार्बामाज़ेपाइन।",
        bn: "ওয়ারফারিন, কার্বামাজেপিন।",
      },
      contraindications: {
        en: "Severe active hepatic impairment or hypersensitivity.",
        hi: "गंभीर लिवर रोग या एलर्जी।",
        bn: "গুরুতর লিভারের রোগ বা অ্যালার্জি।",
      },
      pregnancyWarnings: {
        en: "Use only under obstetric medical supervision.",
        hi: "केवल डॉक्टर की सलाह से उपयोग करें।",
        bn: "শুধুমাত্র চিকিৎসকের পরামর্শে ব্যবহার করুন।",
      },
      storageInstructions: {
        en: "Store below 30°C in a dry place.",
        hi: "30°C से नीचे सूखी जगह पर रखें।",
        bn: "৩০°C-এর নিচে শুষ্ক স্থানে রাখুন।",
      },
    },
    prescriptionRequired: false,
    officialSources: "CDSCO, NLEM 2022, U.S. FDA OpenFDA",
    lastVerifiedDate: "2026-11-20",
    popularScore: 95,
    published: true,
    createdAt: created,
    updatedAt: updated,
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: { rules, host: emulatorHost, port: emulatorPort },
  });
});

after(async () => {
  if (testEnv) await testEnv.cleanup();
});

beforeEach(async () => {
  if (testEnv) await testEnv.clearFirestore();
});

test("Unauthenticated users cannot read or write any collection", async () => {
  const unauth = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauth.collection("medicines").where("published", "==", true).get());
  await assertFails(unauth.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauth.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Unauth",
      preferredLanguage: "en",
      themeMode: "SYSTEM",
      onboardingCompleted: false,
      createdAt: nowMinus(),
      updatedAt: nowMinus(),
    })
  );
});

test("Admin email spoofing with email_verified=false is rejected", async () => {
  const spoofedDb = testEnv.authenticatedContext("spoof_1", {
    email: ADMIN_EMAIL,
    email_verified: false,
  }).firestore();
  const now = nowMinus();
  await assertFails(spoofedDb.collection("medicines").doc("paracetamol_500").set(sampleMedicine(now, now)));
  await assertFails(spoofedDb.collection("apiConnections").get());
});

test("Verified admin can create, update, and list medicines; non-admin can only query published=true", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, {
    email: ADMIN_EMAIL,
    email_verified: true,
  }).firestore();
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = nowMinus(10000);

  // Admin creates medicine (NO `id` field)
  await assertSucceeds(
    adminDb.collection("medicines").doc("paracetamol_500").set(sampleMedicine(created, created))
  );

  // Ghost field rejected even for admin
  await assertFails(
    adminDb.collection("medicines").doc("paracetamol_ghost").set({
      ...sampleMedicine(created, created),
      ghostField: "evil",
    })
  );

  // Non-admin cannot write or update medicines
  await assertFails(
    aliceDb.collection("medicines").doc("paracetamol_2").set(sampleMedicine(created, created))
  );
  await assertFails(
    aliceDb.collection("medicines").doc("paracetamol_500").update({ brandName: "Tampered" })
  );

  // Non-admin filtered query succeeds; unfiltered list fails
  await assertSucceeds(
    aliceDb.collection("medicines").where("published", "==", true).get()
  );
  await assertFails(aliceDb.collection("medicines").get());

  // Admin partial update preserving createdAt succeeds
  await assertSucceeds(
    adminDb.collection("medicines").doc("paracetamol_500").update({
      popularScore: 99,
      updatedAt: nowMinus(1000),
    })
  );
});

test("PriceOffers, categories, manufacturers, and apiConnections enforce admin writes and published reads", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, {
    email: ADMIN_EMAIL,
    email_verified: true,
  }).firestore();
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = nowMinus();

  await assertSucceeds(
    adminDb.collection("priceOffers").doc("offer_1").set({
      medicineId: "paracetamol_500",
      saltComposition: "Paracetamol IP 500 mg",
      brandName: "Jan Aushadhi Paracetamol 500",
      genericName: "Paracetamol",
      genericOption: true,
      packSize: "10 Tablets",
      unitCount: 10,
      mrpInr: 10.0,
      sellingPriceInr: 7.0,
      pricePerUnitInr: 0.7,
      discountPercent: 30,
      manufacturerName: "BPPI / Jan Aushadhi",
      availability: "IN_STOCK",
      sourceName: "PMBJP Jan Aushadhi / NPPA",
      lastUpdatedDate: "2026-11-20",
      published: true,
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    aliceDb.collection("priceOffers").where("published", "==", true).get()
  );
  await assertFails(aliceDb.collection("priceOffers").get());

  await assertSucceeds(
    adminDb.collection("apiConnections").doc("openfda").set({
      name: "OpenFDA Drug Label API",
      baseUrl: "https://api.fda.gov/drug/label.json",
      providerType: "OPENFDA",
      active: true,
      statusMessage: "Connected",
      createdAt: now,
      updatedAt: now,
    })
  );
  // Non-admin cannot read apiConnections
  await assertFails(aliceDb.collection("apiConnections").get());
  await assertSucceeds(adminDb.collection("apiConnections").get());
});

test("User profile and subcollections enforce strict per-user ownership and schema validation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = nowMinus(5000);

  // Alice creates her profile
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      email: "alice@example.com",
      preferredLanguage: "hi",
      themeMode: "LIGHT",
      onboardingCompleted: true,
      createdAt: created,
      updatedAt: created,
    })
  );

  // Alice updates her language
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      preferredLanguage: "bn",
      updatedAt: nowMinus(1000),
    })
  );

  // Bob cannot read or update Alice's profile
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).update({
      displayName: "Hijacked",
      updatedAt: nowMinus(500),
    })
  );

  // Alice manages favorites & reminders
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("favorites").doc("paracetamol_500").set({
      userId: ALICE_UID,
      medicineId: "paracetamol_500",
      medicineName: "Paracetamol 500 mg",
      genericName: "Paracetamol",
      categoryId: "fever_pain",
      createdAt: created,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("favorites").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("favorites").get()
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("reminders").doc("rem_1").set({
      userId: ALICE_UID,
      medicineId: "paracetamol_500",
      medicineName: "Paracetamol 500 mg",
      dosage: "1 Tablet",
      scheduleSlot: "MORNING",
      timeHour: 8,
      timeMinute: 30,
      instructions: "After breakfast",
      active: true,
      createdAt: created,
      updatedAt: created,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("reminders").doc("rem_1").update({
      active: false,
      updatedAt: nowMinus(500),
    })
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("reminders").doc("rem_1").delete()
  );
});

test("AnalyticsEvents can be created by signed-in users for themselves and read only by admin", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, {
    email: ADMIN_EMAIL,
    email_verified: true,
  }).firestore();
  const now = nowMinus();

  await assertSucceeds(
    aliceDb.collection("analyticsEvents").doc("evt_1").set({
      userId: ALICE_UID,
      eventType: "VIEW",
      targetId: "paracetamol_500",
      createdAt: now,
    })
  );

  // Cannot spoof another userId
  await assertFails(
    aliceDb.collection("analyticsEvents").doc("evt_2").set({
      userId: BOB_UID,
      eventType: "VIEW",
      targetId: "paracetamol_500",
      createdAt: now,
    })
  );

  await assertFails(aliceDb.collection("analyticsEvents").get());
  await assertSucceeds(adminDb.collection("analyticsEvents").get());
});
