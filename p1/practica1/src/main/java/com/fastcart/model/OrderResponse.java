package com.fastcart.model;


public class OrderResponse {
    private String status;
    private String message;
    private Order order;
    private Long id =null;

    public OrderResponse(String status, String message, Order order) {
        this.status = status;
        this.message = message;
        this.setOrder(order);
    }
    public OrderResponse(String status, Long id, String message, Order order) {
        this.status = status;
        this.id = id;
        this.message = message;
        this.setOrder(order);
    }
    public OrderResponse(String status, Long id, String message) {
        this.status = status;
        this.id = id;
        this.message = message;
    }


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
		public Order getOrder() {
			return order;
		}
		public void setOrder(Order order) {
			this.order = order;
		}
}
