(ns twentyfour-client-cljs.core
  (:require
   [reagent.core :as r]))

(def settings {:title "Twentyfour"})


;; Define state with some initial values
(def app-state
  (r/atom
   {:count 2
    :wishlist
    [{:id 0 :text "Start learning mindcontrol" :finished true}
     {:id 1 :text "Read a book 'Debugging JS in IE11 without pain'" :finished false}
     {:id 2 :text "Become invisible for a while" :finished false}]}))

;; Update wishlist with provided function and args
(defn update-wishlist [f & args]
  (apply swap! app-state update-in [:wishlist] f args))

(defn add-wish [wish]
  (update-wishlist conj wish))

(defn next-id []
  (swap! app-state update-in [:count] inc))

(defn toggle-item [wish]
  (swap! app-state assoc [:wishlist] 0))


;; Components
(defn page-header [title]
  [:header
   [:h1 title]])

(defn new-item []
  (let [val (r/atom "")
        handle-add-event #(do
                            (add-wish (hash-map :id (:count (next-id)) :text @val))
                            (reset! val ""))]
    (fn []
      [:div
       [:input {:type "text"
                :placeholder "Type your task"
                :value @val
                :on-change #(reset! val (-> % .-target .-value))
                :on-key-press (fn [e]
                                (if (= 13 (.-charCode e))
                                  (handle-add-event)))}]

       [:button {:on-click handle-add-event} "Add"]])))

(defn item [wish]
  ^{:key (:id wish)}
  [:div
   [:span {:class "item-text"} (:text wish)]
   [:i {:class (str "ti-check " (if (:finished wish) "checked" "unchecked"))
        :on-click #(toggle-item (assoc wish :finished true))}]])

(defn items-list []
  [:div
   (for [wish (:wishlist @app-state)]
     (item wish))])

(defn home-page []
  [:div.wrapper
   [page-header (get settings :title)]
   [new-item]
   [:hr]
   [items-list]])


;; Initialize app
(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))