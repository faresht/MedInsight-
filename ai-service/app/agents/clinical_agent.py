"""
Clinical Agent for analyzing clinical and patient data.
Analyzes patient history, demographics, and clinical indicators.
"""
from typing import Dict, Any
import numpy as np
from .base_agent import BaseAgent
import logging

logger = logging.getLogger(__name__)


class ClinicalAgent(BaseAgent):
    """Agent specialized in analyzing clinical and patient data."""
    
    def __init__(self):
        super().__init__(agent_name="ClinicalAgent", confidence_threshold=0.55)
        logger.info("ClinicalAgent initialized")
    
    async def analyze(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analyze clinical data for breast cancer risk assessment.
        
        Args:
            data: Dictionary containing:
                - age: Patient age
                - family_history: Boolean indicating family history of breast cancer
                - previous_biopsies: Number of previous biopsies
                - bmi: Body mass index
                - menopause_status: Pre/post menopausal
                - hormone_therapy: Boolean indicating hormone replacement therapy
                - lifestyle_factors: Dict with smoking, alcohol, etc.
        
        Returns:
            Analysis results with prediction, confidence, and clinical features
        """
        logger.info(f"{self.agent_name}: Starting clinical analysis")
        
        # Extract clinical features
        features = self._extract_clinical_features(data)
        prediction, confidence = self._assess_clinical_risk(features)
        
        # Generate explanation
        explanation = self._generate_explanation(features, prediction, confidence)
        
        result = {
            "agent": self.agent_name,
            "prediction": prediction,
            "confidence": float(confidence),
            "confidence_level": self.get_confidence_level(confidence),
            "features": features,
            "explanation": explanation,
            "risk_factors": self._identify_risk_factors(features)
        }
        
        logger.info(f"{self.agent_name}: Analysis complete - Prediction: {prediction}, Confidence: {confidence:.2f}")
        return result
    
    def _extract_clinical_features(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Extract and normalize clinical features."""
        
        # Get data with defaults
        age = data.get("age", 50)
        family_history = data.get("family_history", False)
        previous_biopsies = data.get("previous_biopsies", 0)
        bmi = data.get("bmi", 25.0)
        menopause_status = data.get("menopause_status", "pre")
        hormone_therapy = data.get("hormone_therapy", False)
        lifestyle = data.get("lifestyle_factors", {})
        
        features = {
            "age": age,
            "age_risk_score": self._calculate_age_risk(age),
            "family_history": family_history,
            "first_degree_relatives": data.get("first_degree_relatives_with_bc", 0),
            "previous_biopsies": previous_biopsies,
            "bmi": bmi,
            "bmi_category": self._categorize_bmi(bmi),
            "postmenopausal": menopause_status.lower() == "post",
            "age_at_first_birth": data.get("age_at_first_birth", 25),
            "hormone_therapy": hormone_therapy,
            "hormone_therapy_duration_years": data.get("hormone_therapy_duration", 0),
            "smoking_status": lifestyle.get("smoking", False),
            "alcohol_consumption": lifestyle.get("alcohol_units_per_week", 0),
            "physical_activity_hours_per_week": lifestyle.get("exercise_hours", 0),
            "breast_density": data.get("breast_density", "scattered"),  # BI-RADS density
            "previous_breast_cancer": data.get("previous_breast_cancer", False),
            "radiation_exposure": data.get("chest_radiation_history", False)
        }
        
        logger.debug(f"Extracted clinical features for age {age}")
        return features
    
    def _calculate_age_risk(self, age: int) -> float:
        """Calculate age-based risk score."""
        if age < 30:
            return 0.1
        elif age < 40:
            return 0.2
        elif age < 50:
            return 0.4
        elif age < 60:
            return 0.6
        elif age < 70:
            return 0.7
        else:
            return 0.8
    
    def _categorize_bmi(self, bmi: float) -> str:
        """Categorize BMI."""
        if bmi < 18.5:
            return "underweight"
        elif bmi < 25:
            return "normal"
        elif bmi < 30:
            return "overweight"
        else:
            return "obese"
    
    def _assess_clinical_risk(self, features: Dict[str, Any]) -> tuple:
        """
        Assess clinical risk based on features.
        
        Returns:
            Tuple of (prediction, confidence)
        """
        risk_score = 0.0
        
        # Age risk
        risk_score += features["age_risk_score"] * 0.2
        
        # Family history (strong risk factor)
        if features["family_history"]:
            risk_score += 0.25
            if features["first_degree_relatives"] >= 2:
                risk_score += 0.15
        
        # Previous conditions
        if features["previous_breast_cancer"]:
            risk_score += 0.3
        if features["previous_biopsies"] > 0:
            risk_score += 0.1 * min(features["previous_biopsies"], 3)
        
        # Hormonal factors
        if features["postmenopausal"] and features["hormone_therapy"]:
            risk_score += 0.1
        if features["age_at_first_birth"] > 30:
            risk_score += 0.05
        
        # Lifestyle factors
        if features["bmi_category"] == "obese":
            risk_score += 0.08
        if features["smoking_status"]:
            risk_score += 0.05
        if features["alcohol_consumption"] > 7:  # More than 7 units/week
            risk_score += 0.05
        
        # Breast density (higher density = higher risk)
        if features["breast_density"] in ["heterogeneously_dense", "extremely_dense"]:
            risk_score += 0.1
        
        # Radiation exposure
        if features["radiation_exposure"]:
            risk_score += 0.08
        
        # Normalize
        risk_score = min(risk_score, 1.0)
        
        # Determine prediction
        if risk_score > 0.5:
            prediction = 1  # High clinical risk
            confidence = min(risk_score + 0.05, 0.90)
        else:
            prediction = 0  # Low clinical risk
            confidence = min(1 - risk_score + 0.05, 0.90)
        
        return prediction, confidence
    
    def _identify_risk_factors(self, features: Dict[str, Any]) -> list:
        """Identify present risk factors."""
        risk_factors = []
        
        if features["age"] > 50:
            risk_factors.append(f"Age {features['age']} years")
        if features["family_history"]:
            risk_factors.append("Family history of breast cancer")
        if features["previous_breast_cancer"]:
            risk_factors.append("Previous breast cancer")
        if features["previous_biopsies"] > 0:
            risk_factors.append(f"{features['previous_biopsies']} previous biopsies")
        if features["bmi_category"] == "obese":
            risk_factors.append(f"Obesity (BMI: {features['bmi']:.1f})")
        if features["hormone_therapy"]:
            risk_factors.append("Hormone replacement therapy")
        if features["breast_density"] in ["heterogeneously_dense", "extremely_dense"]:
            risk_factors.append("High breast density")
        
        return risk_factors
    
    def _generate_explanation(self, features: Dict[str, Any], prediction: int, confidence: float) -> str:
        """Generate human-readable explanation of clinical analysis."""
        
        diagnosis = "elevated clinical risk" if prediction == 1 else "normal clinical risk"
        conf_level = self.get_confidence_level(confidence)
        
        explanation = f"Clinical assessment indicates {diagnosis} with {conf_level} confidence. "
        
        risk_factors = self._identify_risk_factors(features)
        if risk_factors:
            explanation += f"Present risk factors: {', '.join(risk_factors[:3])}. "
        else:
            explanation += "No significant clinical risk factors identified. "
        
        # Add protective factors
        protective = []
        if features["physical_activity_hours_per_week"] >= 3:
            protective.append("regular physical activity")
        if features["bmi_category"] == "normal":
            protective.append("healthy BMI")
        if not features["smoking_status"]:
            protective.append("non-smoker")
        
        if protective:
            explanation += f"Protective factors: {', '.join(protective)}."
        
        return explanation
