(ns todo-list.core
  (:require
   [reagent.core :as r]))

(def settings {:title "Todo List"})


;; Define state with some initial values
(def app-state
  (r/atom
   {:count 2
    :todolist
    [{:id 0 :text "Start learning mindcontrol" :finished true}
     {:id 1 :text "Read a book 'Debugging JS in IE11 without pain'" :finished false}
     {:id 2 :text "Become invisible for a while" :finished false}]}))

;; Update todolist with provided function and args
(defn update-todolist [f & args]
  (apply swap! app-state update-in [:todolist] f args))

(defn add-todo [todo]
  (update-todolist conj todo))

(defn next-id []
  (swap! app-state update-in [:count] inc))

(defn toggle-item [todo]
  (swap! app-state assoc [:todolist] 0))


;; Components
(defn page-header [title]
  [:header
   [:h1 title]])

(defn new-item []
  (let [val (r/atom "")
        handle-add-event #(do
                            (add-todo (hash-map :id (:count (next-id)) :text @val))
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

(defn item [todo]
  ^{:key (:id todo)}
  [:div
   [:span {:class "item-text"} (:text todo)]
   [:i {:class (str "ti-check " (if (:finished todo) "checked" "unchecked"))
        :on-click #(toggle-item (assoc todo :finished true))}]])

(defn items-list []
  [:div
   (for [todo (:todolist @app-state)]
     (item todo))])

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