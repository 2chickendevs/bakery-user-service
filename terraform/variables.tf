variable "docker_image" {
  type        = string
  description = "Docker image, e.g. username/repo:tag"
  default     = "nntan041299/bakery-user-service:latest"
}

variable "app_port" {
  type        = number
  description = "App port exposed by container AND used by ALB target group"
  default     = 8080
}

variable "health_check_path" {
  type        = string
  description = "HTTP path used for ALB health check"
  default     = "/bakery-user-service/api/health-check"
}

variable "env" {
  type        = string
  description = "Current deployed environment"
  default     = "staging"
}

variable "env_docker" {
  type        = map(string)
  description = "Extra env vars / overrides"
  default     = {}
}

variable "ami_id" {
  type        = string
  description = "Ubuntu 20.04 in us-east-1 (change if you want)"
  default     = "ami-011899242bb902164"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}