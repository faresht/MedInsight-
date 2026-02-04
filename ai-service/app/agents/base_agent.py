"""
Base Agent class for the Agentic AI system.
All specialized agents inherit from this base class.
"""
from abc import ABC, abstractmethod
from typing import Dict, Any, Optional
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class BaseAgent(ABC):
    """Abstract base class for all agents in the system."""
    
    def __init__(self, agent_name: str, confidence_threshold: float = 0.5):
        """
        Initialize the base agent.
        
        Args:
            agent_name: Name of the agent
            confidence_threshold: Minimum confidence score for predictions
        """
        self.agent_name = agent_name
        self.confidence_threshold = confidence_threshold
        logger.info(f"Initialized {agent_name} with confidence threshold {confidence_threshold}")
    
    @abstractmethod
    async def analyze(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analyze the provided data and return predictions.
        
        Args:
            data: Input data specific to this agent's domain
            
        Returns:
            Dictionary containing:
                - prediction: The agent's prediction (0=benign, 1=malignant)
                - confidence: Confidence score (0-1)
                - features: Important features identified
                - explanation: Human-readable explanation
        """
        pass
    
    def validate_input(self, data: Dict[str, Any], required_fields: list) -> bool:
        """
        Validate that required fields are present in the input data.
        
        Args:
            data: Input data dictionary
            required_fields: List of required field names
            
        Returns:
            True if all required fields are present, False otherwise
        """
        missing_fields = [field for field in required_fields if field not in data]
        if missing_fields:
            logger.warning(f"{self.agent_name}: Missing required fields: {missing_fields}")
            return False
        return True
    
    def get_confidence_level(self, confidence: float) -> str:
        """
        Convert numerical confidence to categorical level.
        
        Args:
            confidence: Confidence score (0-1)
            
        Returns:
            Confidence level: 'low', 'medium', or 'high'
        """
        if confidence < 0.5:
            return "low"
        elif confidence < 0.75:
            return "medium"
        else:
            return "high"
