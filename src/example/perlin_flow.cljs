(ns example.perlin_flow
  (:require [quil.core :as q]
            [quil.middleware :as m]))


(def w 1200)
(def h 768)
(def ndim 350)
(def size 5000)

(def minAcc (atom -1))
(def maxAcc (atom 1))


(defn particle []
  {:x (* w (Math/random)) :y (* h (Math/random)) :vx 0 :vy 0 :adir 0})


(defn particles [n]
  (map (fn [] (particle)) (range n)))


(defn update-pos [curr delta max]
  (mod (+ curr delta) max))


(defn update-vel [curr delta]
  (q/norm (+ curr delta) 0 2))


(defn update-acc [curr x y]
  (q/map-range (q/noise (/ x ndim) (/ y ndim))
               0
               1
               @minAcc
               @maxAcc))


(defn draw [s]
  (q/fill 100 100 200 50)
  (swap! minAcc (fn [a] (- a 0.1)))
  (swap! maxAcc (fn [a] (+ a 0.1)))
  (doseq [pnt s]
    (q/fill (/ (* 255.0 (:x pnt)) w) (/ (* 255.0 (:y pnt)) h) 200 50)
    (q/ellipse (:x pnt) (:y pnt) 1 1)))


(defn update-particle [p]
  (assoc p
         :x  (update-pos (:x p) (:vx p) w)
         :y  (update-pos (:y p) (:vy p) h)
         :vx (update-vel (:vx p) (Math/cos (:adir p)))
         :vy (update-vel (:vx p) (Math/sin (:adir p)))
         :adir (update-acc (:adir p) (:x p) (:y p))))


(defn create [canvas]
  (q/defsketch perlin-flow
    :host canvas
    :size [w h]
    :setup (fn []
             (q/no-stroke)
             (q/background 20)
             (q/noise-seed (q/random 20))
             (particles size))
    :update (fn [s]
              (map update-particle s))
    :draw draw
    :middleware [m/fun-mode]))