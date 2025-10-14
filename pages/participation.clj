^:kindly/hide-code
(ns participation
  "Participation instructions for Macroexpand 2025"
  (:require [clojure.edn :as edn]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-info (edn/read-string (slurp "info.edn")))

^:kindly/hide-code
(kind/hiccup
 [:div {:style "display: flex; justify-content: flex-end; margin: 1rem 0; padding: 0.5rem; background: #f8f9fa; border-radius: 6px; border: 1px solid #e0e0e0;"}
  [:div {:style "display: flex; align-items: center; gap: 0.5rem;"}
   [:span {:style "font-size: 0.9rem; color: #666;"} "Supported by: "]
   (for [[company-key company-data] (:supporting-companies conference-info)]
     [:a {:key (name company-key)
          :href "./index.html#supporting-companies"
          :style "display: inline-flex; align-items: center; gap: 0.5rem; margin-left: 0.5rem; text-decoration: none;"}
      (when-let [logo-mark (:logo-mark company-data)]
        [:img {:src logo-mark
               :alt (str (:name company-data) " mark")
               :style "height: 20px; width: auto;"}])
      (when-let [logo (:logo company-data)]
        [:img {:src logo
               :alt (str (:name company-data) " logo")
               :style "height: 20px; width: auto;"}])])]])

;; # How to Participate

;; Welcome to Macroexpand 2025! Here's everything you need to get started.

;; ## Getting Your Zoom Link

;; [Register here](https://forms.gle/mQytpTua6RUPcMGb9) (free) to receive calendar invites with Zoom links via email. Check your spam folder if you don't see them!

;; ## Join the Zulip Chat

;; The [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/) is our primary communication channel - essential for discussions, questions, and connecting with others during the conferences.

;; 1. Visit [scicloj.github.io/docs/community/chat](https://clojurians.zulipchat.com/) to join
;; 2. Go to the [#macroexpand-2025 channel](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/)
;; 3. [Introduce yourself in the hello thread](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/topic/hello)!

;; ## Prepare for the Conferences

;;
;; - Install [Zoom](https://zoom.us/download) or use it from your browser, and test your audio/video
;; - Check the schedules: [Macroexpand-Noj](./macroexpand_noj.html) (Oct 17-18) and [Macroexpand-Deep](./macroexpand_deep.html) (Oct 24-25)
;; - Review the [Sessions page](./sessions.html) for talk details
;; - Read our [Code of Conduct](./code_of_conduct.html)

;; ## During the Conferences

;;
;; - Join 5-10 minutes early on the first day
;; - Keep Zulip open for real-time discussions
;; - Mute your microphone when not speaking
;; - Use "Raise Hand" for questions during Q&A
;; - Participate in Open Practice sessions to try out tools hands-on

;; ## Recordings

;; All talks will be recorded and shared on [Scicloj's YouTube channel](https://www.youtube.com/@Scicloj). By participating, you consent to being recorded.

;; ## Need Help?

;; Post in the [#macroexpand-2025 channel](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/) or [contact the organizers](https://scicloj.github.io/docs/community/contact/).

;; ---

;; See you on Zoom and Zulip! 👋
