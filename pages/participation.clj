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

;; # Macroexpand 2025 - Thank You!

;; Thank you for being part of Macroexpand 2025! The conferences have concluded, but the community continues.

;; ## Recordings

;; All talks were recorded and are being published on [Scicloj's YouTube channel](https://www.youtube.com/@Scicloj). Subscribe to get notified when recordings are published!

;; ## Continue the Conversation on Zulip

;; The [#macroexpand-2025](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/) Zulip channel remains active for ongoing discussions!

;; Join to:
;; - Ask questions about the talks and projects
;; - Share insights and follow-up discussions
;; - Connect with speakers and other attendees
;; - Explore collaboration opportunities

;; New to Zulip? Visit [clojurians.zulipchat.com](https://clojurians.zulipchat.com/) to join the Clojurians community.

;; ## Conference Information

;; The conferences took place on:
;; - **[Macroexpand-Noj](./macroexpand_noj.html)**: October 17-18, 2024 - Growing the Noj data science ecosystem
;; - **[Macroexpand-Deep](./macroexpand_deep.html)**: October 24-25, 2024 - The first Clojure AI conference

;; Browse the [Sessions page](./sessions.html) to see all the talks and speakers, or check out the conference schedules for the full program.

;; ## Stay Connected

;; - **Zulip**: Join ongoing discussions in [#macroexpand-2025](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/)
;; - **YouTube**: Subscribe to [Scicloj's channel](https://www.youtube.com/@Scicloj) for recordings
;; - **Community**: Explore the [Scicloj community](https://scicloj.github.io/) and get involved in Clojure data science and AI projects

;; ## Questions?

;; Feel free to post in the [#macroexpand-2025 channel](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/) or [contact the organizers](https://scicloj.github.io/docs/community/contact/).

;; ---

;; Thank you for being part of Macroexpand 2025! 🎉
