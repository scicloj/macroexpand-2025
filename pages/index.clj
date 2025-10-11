^:kindly/hide-code
(ns index
  "Main index page for Macroexpand 2025"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-data
  (edn/read-string (slurp "info.edn")))

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
  | **Ongoing** | Project idea discussions |
  | **September 8, 2025** | Final deadline for talk proposal submissions |
  | **October 17-18, 2025** | [Macroexpand-Noj](./macroexpand_noj.html) Focus: Growing the [Noj](https://scicloj.github.io/noj/) ecosystem |
  | **October 24-25, 2025** | [Macroexpand-Deep](./macroexpand_deep.html) - The first Clojure AI conference |
  
  : {.gradient-table}")

;; ## How to Participate

;; -  **🎟️ Free & Online** - Both conferences are completely free to attend!
;; - **💻 Platform** - Join via Zoom (link provided upon registration)
;; - **💬 Community** - Join the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/) for real-time discussions
;; - **📹 Recordings** - All talks will be recorded and shared publicly afterwards
;; - **⏱️ Format** - Most sessions are 30 minutes talk + 20 minutes discussion
;; - **📝 Registration** - Registration form coming soon! Check back here or follow us on Zulip for updates
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

;; ## Contact

;; [Any questions? Let us talk](https://scicloj.github.io/docs/community/contact/){class="btn btn-gradient" target="_blank"}
