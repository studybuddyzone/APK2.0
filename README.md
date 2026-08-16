# studybuddyzonemanager-android

Manager (admin panel) app — Capacitor WebView wrapper around
`https://manager-public.vercel.app`, connected to the same Firebase project
as StudyBuddyZone main, under package `com.xevrontech.studybuddyzonemanager`.

## Release Setup (ज़रूरी — पहली बार चलाने से पहले)

Yeh एक **नया, independent repo** है — main StudyBuddyZone app वाली
keystore/secrets यहां reuse नहीं होंगी, इसकी अपनी keystore बनानी होगी।

Build चलाने से पहले 4 GitHub Secrets add करने होंगे:

Repo → Settings → Secrets and variables → Actions → New repository secret

| Secret Name | Kya dalna hai |
|---|---|
| `KEYSTORE_BASE64` | Release .jks keystore base64 encoded |
| `KEYSTORE_PASSWORD` | Keystore ka password |
| `KEY_ALIAS` | `studybuddyzonemanager` |
| `KEY_PASSWORD` | Key ka password |

### Keystore बनाने के 2 तरीके:

**Option A — इसी repo के अंदर, GitHub Actions से (mobile-friendly):**
1. Actions tab → **"Generate Release Keystore"** workflow → Run workflow
2. Run पूरी होने पर Artifacts से `release-keystore` zip download करें
3. उसमें `release_base64.txt` का content copy करके `KEYSTORE_BASE64` secret में डालें
4. Password ऊपर workflow file में लिखा हुआ ही रहेगा (`StudyBuddyManager@2026`) — वही
   `KEYSTORE_PASSWORD` और `KEY_PASSWORD` में डालें

**Option B — Laptop से (अगर उपलब्ध हो), ज़्यादा भरोसेमंद:**
```
keytool -genkey -v -keystore studybuddyzonemanager-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias studybuddyzonemanager
```

Secrets add करने के बाद, Actions tab से **"Build Android APK & AAB (Release)"**
workflow चलाएं — signed release APK और AAB दोनों Artifacts में मिलेंगे।

## अलग repo क्यों?
Manager app का package name (`com.xevrontech.studybuddyzonemanager`),
website (`manager-public.vercel.app`), और keystore — सब main StudyBuddyZone
app से independent हैं, इसलिए एक अलग repo रखना सही तरीका है। Firebase
project वही same है (इसलिए `google-services.json` में दोनों apps के
client entries एक साथ दिखेंगे — यह normal है, हर app अपनी entry खुद पढ़
लेता है)।
