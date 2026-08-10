# Google Play checklist for MoodKeyra

Use this checklist immediately before each submission because Google Play rules change.

## Binary

- [ ] Target and compile API meet the current Play requirement. API 36 is configured for the August 2026 requirement.
- [ ] `versionCode` is greater than every previously uploaded artifact.
- [ ] Release AAB is signed with the correct upload key.
- [ ] Release lint and tests pass.
- [ ] No unexpected permission or SDK appears in the merged release manifest or dependency report.
- [ ] Keyboard works offline and the switch-keyboard control is always available.
- [ ] Password, number, email, URL, multiline, Send, Search, and Done fields are tested.

## Privacy and Data safety

- [ ] Public privacy-policy URL is live, stable, accessible without login, and matches the final binary.
- [ ] Privacy policy is also available or clearly summarized inside the app.
- [ ] Play Console Data safety is completed even when no data is collected.
- [ ] Every library and SDK is included in the Data safety audit.
- [ ] No-data-collection declaration is used only if it remains true for the final AAB.

## Store listing

- [ ] Name, short description, full description, icon, screenshots, and feature graphic are original and accurately show MoodKeyra.
- [ ] Listing says moods are manually selected; it does not claim emotion detection, therapy, diagnosis, AI understanding, or message analysis.
- [ ] Listing explains that third-party chat apps receive ordinary text and control their own message styling.
- [ ] No Gboard or Google branding, confusing comparison, copied screenshot, icon, or trade dress is used.
- [ ] Ads, content rating, target audience, app access, and developer contact declarations are accurate.
- [ ] Developer identity and package-name registration requirements shown by Play Console are completed.
- [ ] Brand and trademark screening is repeated before the final listing is created.

## Reviewer instructions

Provide these steps in Play Console:

1. Install and open MoodKeyra.
2. Tap **Set up MoodKeyra** and enable it in Android input-method settings.
3. Return and choose **MoodKeyra Keyboard**.
4. Open any text field and select moods from the strip.
5. Use the `◎` key to switch keyboards.
6. Disable network access and confirm typing and themes still work.

## Final review

- [ ] Test through a Play testing track on representative phones and tablets.
- [ ] Recheck current Developer Program Policies and target API deadlines on submission day.
- [ ] Ensure support email and privacy-policy owner information are real and monitored.

Compliance reduces review risk but cannot guarantee approval; Google evaluates the final app and developer account.
