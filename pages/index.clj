^{:kindly/hide-code true
  :clay {:quarto {:description "Clojure online conferences by Scicloj - October 17-18 & 24-25, 2025"
                  :title-block-style "none"
                  :image "https://scicloj.github.io/macroexpand-2025/images/sci-cloj-logo-transparent.png"
                  :twitter-card {:title "Macroexpand 2025"
                                 :image "https://scicloj.github.io/macroexpand-2025/images/sci-cloj-logo-transparent.png"}
                  :open-graph {:title "Macroexpand 2025"
                               :image "https://scicloj.github.io/macroexpand-2025/images/sci-cloj-logo-transparent.png"}}}}
(ns index
  "Main index page for Macroexpand 2025"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-data
  (edn/read-string (slurp "info.edn")))

^:kindly/hide-code
(kind/hiccup
 [:div {:style "display: flex; justify-content: flex-end; margin: 1rem 0; padding: 0.5rem; background: #f8f9fa; border-radius: 6px; border: 1px solid #e0e0e0;"}
  [:div {:style "display: flex; align-items: center; gap: 0.5rem;"}
   [:span {:style "font-size: 0.9rem; color: #666;"} "Supported by: "]
   (for [[company-key company-data] (:supporting-companies conference-data)]
     [:a {:key (name company-key)
          :href "#supporting-companies"
          :style "display: inline-flex; align-items: center; gap: 0.5rem; margin-left: 0.5rem; text-decoration: none;"}
      (when-let [logo-mark (:logo-mark company-data)]
        [:img {:src logo-mark
               :alt (str (:name company-data) " mark")
               :style "height: 20px; width: auto;"}])
      (when-let [logo (:logo company-data)]
        [:img {:src logo
               :alt (str (:name company-data) " logo")
               :style "height: 20px; width: auto;"}])])]])

;; ![](images/sci-cloj-logo-transparent.svg){fig-alt="Scicloj logo" fig-align="center" width="120px"}

^:kindly/hide-code
(kind/hiccup
 [:div
  ;; Custom styles for people gallery
  [:style "
.people-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
  margin: 2rem 0;
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
}

.small-photo {
  width: 50px !important;
  height: 50px !important;
  border-radius: 50%;
  object-fit: cover;
  transition: transform 0.2s ease;
  border: 2px solid #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.small-photo:hover {
  transform: scale(1.15);
}
"]

  [:div {:class "main-page"}

   ;; Conference intro section
   [:div {:class "conf-intro"}

    [:div {:class "conf-intro__title-wrapper"}
     [:h1 {:class "conf-intro__title"} "Macroexpand 2025"]
     [:div {:class "conf-intro__subtitle"} "Clojure through Data and AI"]]]

   ;; Description
   [:div {:class "conf-intro__description"}
    "Macroexpand 2025 is a pair of online Clojure conferences organised by "
    [:a {:href "https://scicloj.github.io/"} "Scicloj"]
    ". They will focus on two high-priority topics on our journey to make Clojure grow to new fields: "
    [:strong "Noj"] " and " [:strong "AI"] "."

    [:br] [:br]

    "These conferences are related to and informed by the "
    [:a {:href "https://scicloj.github.io/docs/community/groups/macroexpand/"} "Macroexpand Gatherings"]
    " series, which focuses on in-depth discussions around Clojure growth."]]])

^:kindly/hide-code
(kind/hiccup
 [:div {:style "text-align: center; margin: 2rem 0;"}
  [:a {:href "https://forms.gle/mQytpTua6RUPcMGb9"
       :target "_blank"
       :class "btn btn-gradient"
       :style "display: inline-block; text-decoration: none; font-size: 1.1rem; padding: 0.75rem 1.5rem;"
       :aria-label "Register for Macroexpand 2025 conferences - opens in new window"}
   "🎟️ Register Now (Free)"]])

;; ## Our Community

;; Meet the amazing people behind Macroexpand 2025! Click on any photo to learn more about our speakers and organizers on the [People](./people.html) page.

^:kindly/hide-code
(def people-with-images
  (->> (:people conference-data)
       (filter #(:images (second %)))
       (sort-by #(:full-name (second %)))))

^:kindly/hide-code
(kind/md
 (str "::: {.people-gallery}\n"
      (apply str
             (for [[person-key person-data] people-with-images]
               (when-let [images (:images person-data)]
                 (let [image-path (str "images/" (first images))
                       nam (:full-name person-data)
                       anchor (str "#" (name person-key))]
                   (str "[![" nam "](" image-path "){width=\"80px\" height=\"80px\" .person-photo}](./people.html" anchor ")\n")))))
      ":::\n\n"))

;; ## Event Timeline

^:kindly/hide-code
(kind/md
 "| **Timeline** | **Event Details** |
  |--|--|
  | **September 8, 2025** | ~~Final deadline for talk proposal submissions~~ (Closed) |
  | **Ongoing** | Registration open - sign up to attend! |
  | **October 17-18, 2025** | [Macroexpand-Noj](./macroexpand_noj.html) Focus: Growing the [Noj](https://scicloj.github.io/noj/) ecosystem |
  | **October 24-25, 2025** | [Macroexpand-Deep](./macroexpand_deep.html) - The first Clojure AI conference |
  
  : {.gradient-table}")

;; ## How to Participate

;; -  **🎟️ Free & Online** - Both conferences are completely free to attend!
;; - **💻 Platform** - Join via Zoom (link provided upon registration)
;; - **💬 Community** - Join the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/) for real-time discussions
;; - **📹 Recordings** - All talks will be recorded and shared publicly afterwards
;; - **⏱️ Format** - Most sessions are 30 minutes talk + 20 minutes discussion
;; - **📝 Registration** - [Register now for free](https://forms.gle/mQytpTua6RUPcMGb9) to receive Zoom links and updates!
;; - **📢 Call for Proposals** - The proposal deadline has passed (September 8, 2025). Thank you to all who submitted!

;; ## Target Audience

;; The kind of audience we have in mind is of general Clojure programmers. Some talks will also be useful and interesting for people who are new to Clojure as well.

;; ## Goals

;; * Build new open collaborations around key Clojure growth priorities.
;; * Create a welcoming environment for newcomers to get involved.
;; * Strengthen community support by connecting practitioners.
;; * Talk Driven Development - use the conferences as a clear target date for projects to reach sharing milestones.
;; * Build on lessons learned from [SciNoj Light](https://scicloj.github.io/scinoj-light-1/).
;; * Follow our conference-making approach that we 🎥 [recently discussed](https://www.youtube.com/watch?v=n6ICeRyXHsI).

;; ## Chat

;; All communication of the conference will take place at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/). A dedicated channel will be created soon.

;; ## Code of Conduct

;; We are committed to providing a welcoming and inclusive environment for all participants. Please read our [Code of Conduct](./code_of_conduct.html) to learn more about our community standards.

;; ## Supporting Companies {#supporting-companies}

;; We are grateful to the following companies for their support of these conferences:

^:kindly/hide-code
(kind/hiccup
 [:div {:style "margin: 2rem 0; padding: 1.5rem; background: #f8f9fa; border-radius: 8px; border: 1px solid #e0e0e0;"}
  (for [[company-key company-data] (:supporting-companies conference-data)]
    [:div {:key (name company-key) :style "margin-bottom: 1rem; display: flex; align-items: center; gap: 1.5rem;"}
     [:div {:style "display: flex; align-items: center; gap: 0.75rem; flex-shrink: 0;"}
      (when-let [logo-mark (:logo-mark company-data)]
        [:a {:href (:url company-data)
             :target "_blank"}
         [:img {:src logo-mark
                :alt (str (:name company-data) " mark")
                :style "height: 30px; width: auto;"}]])
      (when-let [logo (:logo company-data)]
        [:a {:href (:url company-data)
             :target "_blank"}
         [:img {:src logo
                :alt (str (:name company-data) " logo")
                :style "height: 30px; width: auto;"}]])]
     [:div
      [:h3 {:style "margin: 0 0 0.5rem 0;"}
       [:a {:href (:url company-data)
            :target "_blank"
            :style "color: #4299e1; text-decoration: none;"}
        (:name company-data)]]
      [:p {:style "margin: 0; color: #666;"} (:description company-data)]]])])

;; **Interested in supporting these conferences?** We welcome companies who share our vision of growing the Clojure ecosystem. Please [contact us](https://scicloj.github.io/docs/community/contact/) to discuss how your company can get involved.

;; ## Contact

;; [Any questions? Let us talk](https://scicloj.github.io/docs/community/contact/){class="btn btn-gradient" target="_blank"}
