"""
Imaging Agent for analyzing medical images (mammography, MRI, ultrasound).
Uses CNN-based models for feature extraction and classification.
"""
from typing import Dict, Any
import numpy as np
from .base_agent import BaseAgent
import logging

logger = logging.getLogger(__name__)


class ImagingAgent(BaseAgent):
    """Agent specialized in analyzing medical imaging data."""
    
    def __init__(self):
        super().__init__(agent_name="ImagingAgent", confidence_threshold=0.6)
        self.supported_modalities = ["mammography", "mri", "ultrasound"]
        logger.info(f"ImagingAgent initialized with modalities: {self.supported_modalities}")
    
    async def analyze(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analyze medical images for signs of breast cancer.
        
        Args:
            data: Dictionary containing:
                - image_data: Base64 encoded image or image array
                - modality: Type of imaging (mammography, mri, ultrasound)
                - metadata: Additional image metadata
        
        Returns:
            Analysis results with prediction, confidence, and features
        """
        logger.info(f"{self.agent_name}: Starting image analysis")
        
        # Validate input
        required_fields = ["image_data", "modality"]
        if not self.validate_input(data, required_fields):
            return self._create_error_response("Missing required fields")
        
        modality = data.get("modality", "mammography")
        if modality not in self.supported_modalities:
            logger.warning(f"Unsupported modality: {modality}")
        
        # Simulate image processing and feature extraction
        # In production, this would use a trained CNN model
        features = self._extract_features(data)
        prediction, confidence = self._classify(features)
        
        # Generate explanation
        explanation = self._generate_explanation(features, prediction, confidence)
        
        result = {
            "agent": self.agent_name,
            "prediction": prediction,
            "confidence": float(confidence),
            "confidence_level": self.get_confidence_level(confidence),
            "features": features,
            "explanation": explanation,
            "modality": modality
        }
        
        logger.info(f"{self.agent_name}: Analysis complete - Prediction: {prediction}, Confidence: {confidence:.2f}")
        return result
    
    def _extract_features(self, data: Dict[str, Any]) -> Dict[str, float]:
        """
        Extract relevant features from medical images.
        
        In production, this would use a trained CNN (e.g., ResNet, DenseNet).
        For demonstration, we simulate feature extraction.
        """
        # Simulated features that would be extracted by a CNN
        features = {
            "mass_detected": np.random.random(),
            "microcalcifications": np.random.random(),
            "architectural_distortion": np.random.random(),
            "asymmetry": np.random.random(),
            "density_score": np.random.random(),
            "lesion_size_mm": np.random.uniform(5, 50),
            "lesion_margin_irregularity": np.random.random(),
            "contrast_enhancement": np.random.random()
        }
        
        logger.debug(f"Extracted features: {features}")
        return features
    
    def _classify(self, features: Dict[str, float]) -> tuple:
        """
        Classify based on extracted features.
        
        Returns:
            Tuple of (prediction, confidence)
            prediction: 0 (benign) or 1 (malignant)
            confidence: float between 0 and 1
        """
        # Simulated classification logic
        # In production, this would use a trained model
        
        # Calculate risk score based on features
        risk_score = (
            features["mass_detected"] * 0.3 +
            features["microcalcifications"] * 0.25 +
            features["architectural_distortion"] * 0.2 +
            features["lesion_margin_irregularity"] * 0.15 +
            features["asymmetry"] * 0.1
        )
        
        # Determine prediction and confidence
        if risk_score > 0.5:
            prediction = 1  # Malignant
            confidence = min(risk_score, 0.95)
        else:
            prediction = 0  # Benign
            confidence = min(1 - risk_score, 0.95)
        
        return prediction, confidence
    
    def _generate_explanation(self, features: Dict[str, float], prediction: int, confidence: float) -> str:
        """Generate human-readable explanation of the analysis."""
        
        diagnosis = "malignant" if prediction == 1 else "benign"
        conf_level = self.get_confidence_level(confidence)
        
        # Identify key features
        key_features = []
        if features["mass_detected"] > 0.6:
            key_features.append("suspicious mass detected")
        if features["microcalcifications"] > 0.6:
            key_features.append("microcalcifications present")
        if features["architectural_distortion"] > 0.6:
            key_features.append("architectural distortion observed")
        if features["lesion_margin_irregularity"] > 0.6:
            key_features.append("irregular lesion margins")
        
        if key_features:
            features_text = ", ".join(key_features)
            explanation = f"Imaging analysis suggests {diagnosis} findings with {conf_level} confidence. Key observations: {features_text}."
        else:
            explanation = f"Imaging analysis suggests {diagnosis} findings with {conf_level} confidence. No significant abnormalities detected."
        
        return explanation
    
    def _create_error_response(self, error_message: str) -> Dict[str, Any]:
        """Create error response dictionary."""
        return {
            "agent": self.agent_name,
            "error": error_message,
            "prediction": None,
            "confidence": 0.0
        }
