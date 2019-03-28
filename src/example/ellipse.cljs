(ns example.ellipse
  (:require [quil.core :as q]
            [quil.middleware :as m]))


(def size 300)


(defn setup []
  (q/background 255 25 0)
  (q/frame-rate 600)
  ; (q/fill 255 255 0)
  {:diameter 20
   :r 0
   :g 0
   :b 0
   :x 0
   :y 0})

(q/noise 42)

(defn update-state [state]
  (-> state
      (assoc :diameter
             (mod (+ 1
                     (:diameter state))
                  255))
      (assoc :y
             (mod (+ (:y state) 1) size))
      (assoc :r
             (mod (+ 1 (:r state)) 255))
      (assoc :g
             (mod (+ 2 (:g state)) 255))
      (assoc :b
             (mod (+ 3 (:b state)) 255))))


(defn draw [state]
  (q/background 255 255 0)
  (q/fill (:r state) (:g state) (:b state))
  (q/ellipse (:x state)
             (:y state)
             (:diameter state)
             (:diameter state)))


(defn create [canvas]
  (q/defsketch ellipse
    :host canvas
    :size [size size]
    :setup setup
    :update update-state
    :draw draw
    :middleware [m/fun-mode]))